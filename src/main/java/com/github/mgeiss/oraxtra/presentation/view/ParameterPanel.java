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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class ParameterPanel extends JPanel {

    private int bindVariables;
    private Class<?>[] classArray = new Class<?>[]{
        String.class, Number.class, Date.class
    };
    private LinkedList<JComboBox> classFields;
    private LinkedList<JTextField> variableFields;

    public ParameterPanel(int bindVariables) {
        super();
        this.bindVariables = bindVariables;
        this.init();
    }

    private void init() {
        super.setLayout(new GridBagLayout());
        this.classFields = new LinkedList<>();
        this.variableFields = new LinkedList<>();

        JComboBox classComboBox = null;
        JTextField variableField = null;
        Dimension size = new Dimension(140, 21);

        for (int i = 0; i < bindVariables; i++) {
            classComboBox = new JComboBox(this.classArray);
            this.classFields.add(classComboBox);
            super.add(classComboBox,
                    new GridBagConstraints(0, i, 1, 1, 0.50D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
            classComboBox.setPreferredSize(size);

            variableField = new JTextField();
            this.variableFields.add(variableField);
            super.add(variableField,
                    new GridBagConstraints(1, i, 1, 1, 0.50D, 0.00D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
            variableField.setPreferredSize(size);
        }
    }

    public Class<?> getClass(int index) {
        return (Class<?>) this.classFields.get(index).getSelectedItem();
    }

    public String getVariable(int index) {
        return this.variableFields.get(index).getText();
    }
}
