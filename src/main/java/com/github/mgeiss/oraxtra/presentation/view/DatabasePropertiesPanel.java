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

import com.github.mgeiss.oraxtra.presentation.control.OraXTraController;
import com.github.mgeiss.oraxtra.util.Messages;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Properties;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author mgeiss
 * @version 1.0
 */
public class DatabasePropertiesPanel extends JPanel {

    private Properties connectionProperties;
    private JTextField hostField;
    private JTextField portField;
    private JTextField sidField;
    private JTextField userField;
    private JPasswordField passwordField;

    public DatabasePropertiesPanel(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
        this.init();
    }

    public Properties createConnectionProperties() {
        Properties newConnectionProperties = new Properties();

        newConnectionProperties.put(OraXTraController.PROPERTY_DB_HOST, this.hostField.getText());
        newConnectionProperties.put(OraXTraController.PROPERTY_DB_PORT, this.portField.getText());
        newConnectionProperties.put(OraXTraController.PROPERTY_DB_SID, this.sidField.getText());
        newConnectionProperties.put(OraXTraController.PROPERTY_DB_USER, this.userField.getText());
        newConnectionProperties.put(OraXTraController.PROPERTY_DB_PWD, new String(this.passwordField.getPassword()));

        return newConnectionProperties;
    }

    private void init() {
        
        super.setLayout(new GridBagLayout());
        super.add(new JLabel(Messages.getText("oraxtra.dbproperties.panel.host")), new GridBagConstraints(0, 0, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        this.hostField = new JTextField();
        super.add(this.hostField, new GridBagConstraints(1, 0, 1, 1, 1.00D, 1.00D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        if (this.connectionProperties != null) {
            this.hostField.setText(this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_HOST));
        }

        super.add(new JLabel(Messages.getText("oraxtra.dbproperties.panel.port")), new GridBagConstraints(0, 1, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        this.portField = new JTextField();
        super.add(this.portField, new GridBagConstraints(1, 1, 1, 1, 1.00D, 1.00D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        if (this.connectionProperties != null) {
            this.portField.setText(this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_PORT));
        }

        super.add(new JLabel(Messages.getText("oraxtra.dbproperties.panel.sid")), new GridBagConstraints(0, 2, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        this.sidField = new JTextField();
        super.add(this.sidField, new GridBagConstraints(1, 2, 1, 1, 1.00D, 1.00D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        if (this.connectionProperties != null) {
            this.sidField.setText(this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_SID));
        }

        super.add(new JLabel(Messages.getText("oraxtra.dbproperties.panel.user")), new GridBagConstraints(0, 3, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        this.userField = new JTextField();
        super.add(this.userField, new GridBagConstraints(1, 3, 1, 1, 1.00D, 1.00D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        if (this.connectionProperties != null) {
            this.userField.setText(this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_USER));
        }

        super.add(new JLabel(Messages.getText("oraxtra.dbproperties.panel.password")), new GridBagConstraints(0, 4, 1, 1, 0.00D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        this.passwordField = new JPasswordField();
        super.add(this.passwordField, new GridBagConstraints(1, 4, 1, 1, 1.00D, 1.00D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        if (this.connectionProperties != null) {
            this.passwordField.setText(this.connectionProperties.getProperty(OraXTraController.PROPERTY_DB_PWD));
        }
    }
}
