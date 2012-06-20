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
package com.github.mgeiss.oraxtra.domain;

/**
 *
 * @author Markus Geiss
 * @verison 1.0
 */
public class PlanElement {

    private String operation;
    private String options;
    private String objectName;
    private int depth;
    private String accessPredicates;
    private String filterPredicates;

    public PlanElement() {
        super();
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setAccessPredicates(String accessPredicates) {
        this.accessPredicates = accessPredicates;
    }

    public void setFilterPredicates(String filterPredicates) {
        this.filterPredicates = filterPredicates;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.depth; i++) {
            sb.append("\t");
        }
        sb.append(this.operation);
        sb.append(" ");
        sb.append(this.options);
        sb.append(" ");
        sb.append(this.objectName);
        sb.append(" (");
        sb.append(this.accessPredicates);
        sb.append(", ");
        sb.append(this.filterPredicates);
        sb.append(")");

        return sb.toString();
    }
}
