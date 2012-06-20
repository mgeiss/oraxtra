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
package com.github.mgeiss.oraxtra.presentation.view;

import com.github.mgeiss.oraxtra.domain.ExecutionPlan;
import com.github.mgeiss.oraxtra.domain.SQLText;
import com.github.mgeiss.oraxtra.presentation.control.OraXTraController;
import com.github.mgeiss.oraxtra.presentation.model.QueryResultTableModel;
import com.github.mgeiss.oraxtra.presentation.model.SQLSyntaxDocument;
import com.github.mgeiss.oraxtra.presentation.model.SQLTextTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.table.TableModel;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class OraXTraFrame extends JFrame {

    private OraXTraController controller;
    private JMenuItem exitMenuItem;
    private JMenuItem runMenuItem;
    private JMenuItem databaseMenuItem;
    private JTextPane sqlCommandPane;
    private JTable resultTable;
    private SQLTextTableModel sqlTextTableModel;
    private JTextPane executionPlanPane;
    private JLabel statusLabel;
    private JProgressBar statusBar;
    private ImageIcon appIcon;
    private ImageIcon exitIcon;
    private ImageIcon runIcon;
    private ImageIcon runPendingIcon;
    private ImageIcon dbProbsIcon;

    public OraXTraFrame(OraXTraController controller) {
        super();
        this.controller = controller;
        this.init();
    }

    public void setSQLTextTableData(SQLText data) {
        this.sqlTextTableModel.setData(data);
    }

    public void clearSQLTextTable() {
        this.sqlTextTableModel.clearData();
    }

    public void setExecutionPlan(ExecutionPlan executionPlan) {
        this.executionPlanPane.setText(executionPlan.toString());
    }

    public void clearExecutionPlan() {
        this.executionPlanPane.setText(null);
    }

    public void setResultTableModel(TableModel tableModel) {
        this.resultTable.setModel(tableModel);
    }

    public String select() {
        return this.sqlCommandPane.getText();
    }

    public void clearResultTable() {
        this.resultTable.setModel(new QueryResultTableModel());
    }

    public void startProgress(final String text) {
        SwingUtilities.invokeLater(new Thread() {

            @Override
            public void run() {
                OraXTraFrame.this.statusLabel.setText(text);
                OraXTraFrame.this.statusBar.setIndeterminate(true);
                OraXTraFrame.this.runMenuItem.setEnabled(false);
                OraXTraFrame.this.runMenuItem.setIcon(OraXTraFrame.this.runPendingIcon);
                OraXTraFrame.this.exitMenuItem.setEnabled(false);
                OraXTraFrame.this.databaseMenuItem.setEnabled(false);
            }
        });
    }

    public void stopProgress() {
        SwingUtilities.invokeLater(new Thread() {

            @Override
            public void run() {
                OraXTraFrame.this.statusLabel.setText("");
                OraXTraFrame.this.statusBar.setIndeterminate(false);
                OraXTraFrame.this.runMenuItem.setEnabled(true);
                OraXTraFrame.this.runMenuItem.setIcon(OraXTraFrame.this.runIcon);
                OraXTraFrame.this.exitMenuItem.setEnabled(true);
                OraXTraFrame.this.databaseMenuItem.setEnabled(true);
            }
        });
    }

    private void init() {
        
        this.appIcon = new ImageIcon(ClassLoader.getSystemResource("icons/db_status.png"));
        this.exitIcon = new ImageIcon(ClassLoader.getSystemResource("icons/exit.png"));
        this.runIcon = new ImageIcon(ClassLoader.getSystemResource("icons/cnr.png"));
        this.runPendingIcon = new ImageIcon(ClassLoader.getSystemResource("icons/cnr-pending.png"));
        this.dbProbsIcon = new ImageIcon(ClassLoader.getSystemResource("icons/edit.png"));
        
        super.setTitle("OraXTra - Oracle Execution Trace");
        super.setIconImage(this.appIcon.getImage());
        
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        super.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        this.exitMenuItem = new JMenuItem("Exit", this.exitIcon);
        fileMenu.add(exitMenuItem);
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        exitMenuItem.addActionListener(this.controller);
        exitMenuItem.setActionCommand(OraXTraController.ACTION_EXIT);

        JMenu executeMenu = new JMenu("Execute");
        menuBar.add(executeMenu);
        executeMenu.setMnemonic(KeyEvent.VK_X);

        this.runMenuItem = new JMenuItem("Run", this.runIcon);
        executeMenu.add(this.runMenuItem);
        this.runMenuItem.setMnemonic(KeyEvent.VK_R);
        this.runMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        this.runMenuItem.addActionListener(this.controller);
        this.runMenuItem.setActionCommand(OraXTraController.ACTION_RUN);

        JMenu preferenceMenu = new JMenu("Preferences");
        menuBar.add(preferenceMenu);
        preferenceMenu.setMnemonic(KeyEvent.VK_P);

        this.databaseMenuItem = new JMenuItem("Database Properties", this.dbProbsIcon);
        preferenceMenu.add(databaseMenuItem);
        databaseMenuItem.setMnemonic(KeyEvent.VK_D);
        databaseMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK));
        databaseMenuItem.addActionListener(this.controller);
        databaseMenuItem.setActionCommand(OraXTraController.ACTION_PREF_DB);

        JPanel contentPanel = new JPanel(new BorderLayout());
        super.setContentPane(contentPanel);

        JPanel topPanel = new JPanel(new BorderLayout());
        contentPanel.add(topPanel, BorderLayout.NORTH);
        topPanel.setBorder(BorderFactory.createTitledBorder("SQL Command"));
        topPanel.setPreferredSize(new Dimension(100, 100));

        this.sqlCommandPane = new JTextPane();
        topPanel.add(new JScrollPane(this.sqlCommandPane), BorderLayout.CENTER);
        this.sqlCommandPane.setDocument(new SQLSyntaxDocument());

        JPanel centerPanel = new JPanel(new BorderLayout());
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createTitledBorder("Result"));

        this.resultTable = new JTable();
        centerPanel.add(new JScrollPane(this.resultTable), BorderLayout.CENTER);

        JPanel tracePanel = new JPanel(new BorderLayout());
        centerPanel.add(tracePanel, BorderLayout.SOUTH);
        tracePanel.setBorder(BorderFactory.createTitledBorder("Trace Information"));

        JTabbedPane tabbedPane = new JTabbedPane();
        tracePanel.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.setPreferredSize(new Dimension(100, 150));

        this.sqlTextTableModel = new SQLTextTableModel();
        JTable sqlTextTable = new JTable(this.sqlTextTableModel);
        tabbedPane.addTab("SQL Text", new JScrollPane(sqlTextTable));

        this.executionPlanPane = new JTextPane();
        this.executionPlanPane.setEditable(false);
        tabbedPane.addTab("Execution Plan", new JScrollPane(this.executionPlanPane));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));

        this.statusLabel = new JLabel();
        bottomPanel.add(this.statusLabel);

        JSeparator verticalSeparator = new JSeparator(JSeparator.VERTICAL);
        verticalSeparator.setPreferredSize(new Dimension(2, 20));

        bottomPanel.add(verticalSeparator);

        this.statusBar = new JProgressBar();
        bottomPanel.add(this.statusBar);
        this.statusBar.setPreferredSize(new Dimension(100, 20));
    }
}
