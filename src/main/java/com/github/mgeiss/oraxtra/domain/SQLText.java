/*
 * Copyright 2012 mgeiss.
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
 * @author mgeiss
 */
public class SQLText {

    private String optimizerMode;
    private String statement;
    private String address;
    private long rowsProcessed;
    private long elapsedTime;
    private long cpuTime;
    private long userIoWaitTime = Long.MIN_VALUE;
    private long bufferGets;
    private long diskReads;

    public SQLText() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getBufferGets() {
        return bufferGets;
    }

    public void setBufferGets(long bufferGets) {
        this.bufferGets = bufferGets;
    }

    public long getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(long cpuTime) {
        this.cpuTime = cpuTime;
    }

    public long getDiskReads() {
        return diskReads;
    }

    public void setDiskReads(long diskReads) {
        this.diskReads = diskReads;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getOptimizerMode() {
        return optimizerMode;
    }

    public void setOptimizerMode(String optimizerMode) {
        this.optimizerMode = optimizerMode;
    }

    public long getRowsProcessed() {
        return rowsProcessed;
    }

    public void setRowsProcessed(long rowsProcessed) {
        this.rowsProcessed = rowsProcessed;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public long getUserIoWaitTime() {
        return userIoWaitTime;
    }

    public void setUserIoWaitTime(long userIoWaitTime) {
        this.userIoWaitTime = userIoWaitTime;
    }
}
