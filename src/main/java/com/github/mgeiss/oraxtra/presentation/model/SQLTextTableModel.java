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
package com.github.mgeiss.oraxtra.presentation.model;

import com.github.mgeiss.oraxtra.domain.SQLText;
import com.github.mgeiss.oraxtra.util.Messages;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class SQLTextTableModel extends AbstractTableModel {

    private SQLText data;

    public void setData(SQLText data) {
        this.data = data;
        super.fireTableDataChanged();
    }

    public void clearData() {
        this.data = null;
        super.fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public int getRowCount() {
        return (this.data != null ? 1 : 0);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = this.data.getOptimizerMode();
                break;
            case 1:
                value = this.data.getRowsProcessed();
                break;
            case 2:
                value = this.data.getElapsedTime() / 1000;
                break;
            case 3:
                value = this.data.getCpuTime() / 1000;
                break;
            case 4:
                value = (this.data.getUserIoWaitTime() != Long.MIN_VALUE ? this.data.getUserIoWaitTime() / 1000 : "N/A");
                break;
            case 5:
                value = this.data.getDiskReads();
                break;
            case 6:
                value = this.data.getBufferGets();
                break;
        }

        return value;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> clazz = null;

        switch (columnIndex) {
            case 0:
                clazz = String.class;
                break;
            case 1:
                clazz = Long.class;
                break;
            case 2:
                clazz = Long.class;
                break;
            case 3:
                clazz = Long.class;
                break;
            case 4:
                clazz = Long.class;
                break;
            case 5:
                clazz = Long.class;
                break;
            case 6:
                clazz = Long.class;
                break;
        }

        return clazz;
    }

    @Override
    public String getColumnName(int columnIndex) {
        String name = null;
        switch (columnIndex) {
            case 0:
                name = Messages.getText("oraxtra.sqltexttablemodel.column.optimizermode");
                break;
            case 1:
                name = Messages.getText("oraxtra.sqltexttablemodel.column.rowsprocessed");
                break;
            case 2:
                name = Messages.getText("oraxtra.sqltexttablemodel.column.elapsedtime");
                break;
            case 3:
                name = Messages.getText("oraxtra.sqltexttablemodel.column.cputime");
                break;
            case 4:
                name = Messages.getText("oraxtra.sqltexttablemodel.column.userwait");
                break;
            case 5:
                name = Messages.getText("oraxtra.sqltexttablemodel.column.diskreads");
                break;
            case 6:
                name = Messages.getText("oraxtra.sqltexttablemodel.column.buffergets");
                break;
        }
        return name;
    }
}
