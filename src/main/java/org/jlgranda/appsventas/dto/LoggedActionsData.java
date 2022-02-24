/*
 * Licensed under the TECNOPRO License, Version 1.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.tecnopro.net/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jlgranda.appsventas.dto;

import java.math.BigInteger;
import java.util.Date;
/**
 *
 * @author jlgranda
 */
public class LoggedActionsData {

    private Long eventId;
    private String schemaName;
    private String tableName;
    private long relid;
    private String sessionUserName;
    private Date actionTstampTx;
    private Date actionTstampStm;
    private Date actionTstampClk;
    private BigInteger transactionId;
    private String applicationName;
//    private Object clientAddr;
    private Integer clientPort;
    private String clientQuery;
    private String action;
    private String rowData;
    private String changedFields;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getRelid() {
        return relid;
    }

    public void setRelid(long relid) {
        this.relid = relid;
    }

    public String getSessionUserName() {
        return sessionUserName;
    }

    public void setSessionUserName(String sessionUserName) {
        this.sessionUserName = sessionUserName;
    }

    public Date getActionTstampTx() {
        return actionTstampTx;
    }

    public void setActionTstampTx(Date actionTstampTx) {
        this.actionTstampTx = actionTstampTx;
    }

    public Date getActionTstampStm() {
        return actionTstampStm;
    }

    public void setActionTstampStm(Date actionTstampStm) {
        this.actionTstampStm = actionTstampStm;
    }

    public Date getActionTstampClk() {
        return actionTstampClk;
    }

    public void setActionTstampClk(Date actionTstampClk) {
        this.actionTstampClk = actionTstampClk;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public String getClientQuery() {
        return clientQuery;
    }

    public void setClientQuery(String clientQuery) {
        this.clientQuery = clientQuery;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRowData() {
        return rowData;
    }

    public void setRowData(String rowData) {
        this.rowData = rowData;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }
 
}