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
package com.github.mgeiss.oraxtra;

import com.github.mgeiss.oraxtra.presentation.control.OraXTraController;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * <code>OraXTra</code> stands for ORAcle eXecution TRAce.
 * <code>OraXTra</code> A little tool to trace the execution plan for a given
 * database query.
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class OraXTra {

    public OraXTra() {
        super();
    }

    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Could not find suitable JDBC-Driver, programm wil exit.\n" + ex.getClass().getSimpleName() + ": " + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        try {
            Class<?> clazz = Class.forName("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
            Method setPlasticTheme = clazz.getMethod("setPlasticTheme", Class.forName("com.jgoodies.looks.plastic.PlasticTheme"));
            setPlasticTheme.invoke(clazz.newInstance(), Class.forName("com.jgoodies.looks.plastic.theme.ExperienceGreen").newInstance());
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | UnsupportedLookAndFeelException ex) {
        }
        
        new OraXTraController();
    }
}
