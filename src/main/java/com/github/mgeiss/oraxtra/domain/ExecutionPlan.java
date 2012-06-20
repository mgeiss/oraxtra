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

import java.util.LinkedList;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class ExecutionPlan {

    private LinkedList<PlanElement> elements = new LinkedList<>();

    public ExecutionPlan() {
        super();
    }

    public void add(PlanElement planElement) {
        this.elements.add(planElement);
    }

    public void clear() {
        this.elements.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (PlanElement planElement : this.elements) {
            sb.append(planElement.toString());
            sb.append(System.getProperty("line.separator"));
        }

        return sb.toString();
    }
}
