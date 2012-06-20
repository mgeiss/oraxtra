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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class QueryResultTableModel extends AbstractTableModel {

    private LinkedList<Class<?>> columnClasses;
    private LinkedList<String> columns;
    private LinkedList<LinkedHashMap<String, Object>> data;

    public QueryResultTableModel() {
        super();
    }

    public void setColumnClasses(LinkedList<Class<?>> columnClasses) {
        this.columnClasses = columnClasses;
    }

    public void setColumns(LinkedList<String> columns) {
        this.columns = columns;
    }

    public void setData(LinkedList<LinkedHashMap<String, Object>> data) {
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        return (this.columns != null ? columns.size() : 0);
    }

    @Override
    public int getRowCount() {
        return (this.data != null ? data.size() : 0);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LinkedHashMap<String, Object> row = this.data.get(rowIndex);
        return row.get(this.columns.get(columnIndex));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.columnClasses.get(columnIndex);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return this.columns.get(columnIndex);
    }
}
