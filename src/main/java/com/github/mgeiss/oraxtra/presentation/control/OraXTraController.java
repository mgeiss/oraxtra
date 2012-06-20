/*
 * Copyright 2012 Markus Geiss.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mgeiss.oraxtra.presentation.control;

import com.github.mgeiss.oraxtra.domain.ExecutionPlan;
import com.github.mgeiss.oraxtra.domain.PlanElement;
import com.github.mgeiss.oraxtra.domain.SQLText;
import com.github.mgeiss.oraxtra.presentation.model.QueryResultTableModel;
import com.github.mgeiss.oraxtra.presentation.view.DatabasePropertiesPanel;
import com.github.mgeiss.oraxtra.presentation.view.OraXTraFrame;
import com.github.mgeiss.oraxtra.presentation.view.ParameterPanel;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class OraXTraController implements ActionListener {

    public static final String ACTION_EXIT = "actionExit";
    public static final String ACTION_RUN = "actionRun";
    public static final String ACTION_PREF_DB = "actionPrefDb";
    public static final String DRIVER_INFO = "jdbc:oracle:thin:@";
    public static final String PROPERTY_DB_HOST = "db.host";
    public static final String PROPERTY_DB_PORT = "db.port";
    public static final String PROPERTY_DB_SID = "db.sid";
    public static final String PROPERTY_DB_USER = "db.user";
    public static final String PROPERTY_DB_PWD = "db.pwd";
    private OraXTraFrame frame;
    private Properties connectionProperties;

    public OraXTraController() {
        super();
        this.init();
    }

    private void init() {
        this.frame = new OraXTraFrame(this);

        this.frame.setSize(new Dimension(1024, 768));
        this.frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(new Thread() {

            @Override
            public void run() {
                OraXTraController.this.frame.setVisible(true);
            }
        });
    }

    private boolean checkConnectionProperties() {
        boolean valid = false;
        try {
            Connection connection = DriverManager.getConnection(this.jdbcUrl(), this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_USER), this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_PWD));
            connection.close();
            valid = true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.frame, ex.getClass().getSimpleName() + ": " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
        return valid;
    }

    private String jdbcUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(OraXTraController.DRIVER_INFO);
        sb.append(this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_HOST));
        sb.append(":");
        sb.append(this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_PORT));
        sb.append(":");
        sb.append(this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_SID));

        return sb.toString();
    }

    private void execute() {
        if (!this.frame.select().trim().toLowerCase().startsWith("select")) {
            JOptionPane.showMessageDialog(this.frame, "Only queries are supported!");
            return;
        }
        this.frame.startProgress("Run...");
        this.frame.clearResultTable();
        this.frame.clearSQLTextTable();
        this.frame.clearExecutionPlan();
        
        try {
            if (this.connectionProperties == null) {
                this.frame.startProgress("Enter database information");
                this.showDataBasePropertiesDialog();
                if (!this.checkConnectionProperties()) {
                    this.frame.stopProgress();
                    return;
                }
            }

            String timestamp = "/*" + System.currentTimeMillis() + "*/";

            int bindVariables = 0;
            if (this.frame.select().indexOf("?") >= 0) {
                StringTokenizer tokenizer = new StringTokenizer(this.frame.select(), "?");
                bindVariables = tokenizer.countTokens();
                if ((this.frame.select().lastIndexOf("?") + 1) != this.frame.select().length()) {
                    bindVariables--;
                }
            }
            
            try (Connection connection = DriverManager.getConnection(this.jdbcUrl(), this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_USER), this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_PWD))) {
                Statement stmt = connection.createStatement();
                PreparedStatement pstmt = null;

                stmt.execute("alter session set events = 'immediate trace name flush_cache'");

                ResultSet rset = null;
                if (bindVariables > 0) {
                    this.frame.startProgress("Enter bind variables");
                    ParameterPanel panel = new ParameterPanel(bindVariables);
                    if (JOptionPane.showOptionDialog(this.frame, panel, "Parameter", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {
                        pstmt = connection.prepareStatement(this.frame.select() + timestamp);
                        Class<?> clazz = null;
                        String variable = null;
                        for (int i = 0; i < bindVariables; i++) {
                            clazz = panel.getClass(i);
                            variable = panel.getVariable(i);
                            if (clazz.equals(Date.class)) {
                                DateFormat format = DateFormat.getDateTimeInstance();
                                pstmt.setTimestamp((i + 1), new Timestamp(format.parse(variable).getTime()));
                            } else if (clazz.equals(String.class)) {
                                pstmt.setString((i + 1), variable);
                            } else if (clazz.equals(Number.class)) {
                                pstmt.setBigDecimal((i + 1), new BigDecimal(variable));
                            }
                        }
                        rset = pstmt.executeQuery();
                    } else {
                        JOptionPane.showMessageDialog(this.frame, "No parameter specified, abort execution!");
                        stmt.close();
                        connection.close();
                        this.frame.stopProgress();
                        return;
                    }
                } else {
                    stmt = connection.createStatement();
                    rset = stmt.executeQuery(this.frame.select() + timestamp);
                }
                this.frame.startProgress("Execute query");
                ResultSetMetaData rsetMetaData = rset.getMetaData();

                LinkedList<String> columns = new LinkedList<>();
                LinkedList<Class<?>> columnClasses = new LinkedList<>();
                for (int i = 1; i <= rsetMetaData.getColumnCount(); i++) {
                    columns.add(rsetMetaData.getColumnLabel(i));
                    columnClasses.add(Class.forName(rsetMetaData.getColumnClassName(i)));
                }

                LinkedHashMap<String, Object> row = null;
                LinkedList<LinkedHashMap<String, Object>> data = new LinkedList<>();
                int rowCount = 0;
                while (rset.next()) {
                    if (rowCount < 100) {
                        row = new LinkedHashMap<>();
                        data.add(row);
                        for (String columnName : columns) {
                            row.put(columnName, rset.getObject(columnName));
                        }
                        rowCount++;
                    }
                }

                QueryResultTableModel tableModel = new QueryResultTableModel();
                tableModel.setColumnClasses(columnClasses);
                tableModel.setColumns(columns);
                tableModel.setData(data);

                this.frame.setResultTableModel(tableModel);

                rset.close();

                this.frame.startProgress("Determine execution information");

                Statement stmtSql = connection.createStatement();
                rset = stmtSql.executeQuery("select optimizer_mode, address, disk_reads, buffer_gets, rows_processed, cpu_time, elapsed_time, user_io_wait_time from v$sql where sql_text like '%" + timestamp + "'");

                SQLText sqlText = new SQLText();
                if (rset.next()) {
                    sqlText.setOptimizerMode(rset.getString("OPTIMIZER_MODE"));
                    sqlText.setAddress(rset.getString("ADDRESS"));
                    sqlText.setBufferGets(rset.getLong("BUFFER_GETS"));
                    sqlText.setCpuTime(rset.getLong("CPU_TIME"));
                    sqlText.setDiskReads(rset.getLong("DISK_READS"));
                    sqlText.setElapsedTime(rset.getLong("ELAPSED_TIME"));
                    sqlText.setRowsProcessed(rset.getLong("ROWS_PROCESSED"));
                    sqlText.setUserIoWaitTime(rset.getLong("USER_IO_WAIT_TIME"));
                }

                rset.close();
                stmtSql.close();

                this.frame.setSQLTextTableData(sqlText);

                if (sqlText.getAddress() != null) {
                    ExecutionPlan executionPlan;
                    try (PreparedStatement pstmtPlan = connection.prepareStatement("select operation, options, object_name, depth,  access_predicates, filter_predicates from v$sql_plan where address = ? order by id")) {
                        pstmtPlan.setString(1, sqlText.getAddress());
                        rset = pstmtPlan.executeQuery();
                        executionPlan = new ExecutionPlan();
                        PlanElement planElement = null;
                        while (rset.next()) {
                            planElement = new PlanElement();
                            planElement.setAccessPredicates(rset.getString("ACCESS_PREDICATES"));
                            planElement.setDepth(rset.getInt("DEPTH"));
                            planElement.setFilterPredicates(rset.getString("FILTER_PREDICATES"));
                            planElement.setObjectName(rset.getString("OBJECT_NAME"));
                            planElement.setOperation(rset.getString("OPERATION"));
                            planElement.setOptions(rset.getString("OPTIONS"));
                            executionPlan.add(planElement);
                        }
                        rset.close();
                    }

                    this.frame.setExecutionPlan(executionPlan);
                }

                if (pstmt != null) {
                    pstmt.close();
                }

                stmt.close();
            }
        } catch (SQLException | HeadlessException | ParseException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this.frame, ex.getClass().getSimpleName() + ": " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
        this.frame.stopProgress();
    }

    private void showDataBasePropertiesDialog() {
        DatabasePropertiesPanel panel = new DatabasePropertiesPanel(this.connectionProperties);
        if (JOptionPane.showOptionDialog(this.frame, panel, "Database Properties", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {
            this.connectionProperties = panel.createConnectionProperties();
            this.frame.clearResultTable();
            this.frame.clearSQLTextTable();
            this.frame.clearExecutionPlan();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case OraXTraController.ACTION_EXIT:
                System.exit(0);
                break;
            case OraXTraController.ACTION_PREF_DB:
                this.showDataBasePropertiesDialog();
                this.checkConnectionProperties();
                break;
            case OraXTraController.ACTION_RUN:
                new Thread() {

                    @Override
                    public void run() {
                        OraXTraController.this.execute();
                    }
                }.start();
                break;
        }
    }
}
