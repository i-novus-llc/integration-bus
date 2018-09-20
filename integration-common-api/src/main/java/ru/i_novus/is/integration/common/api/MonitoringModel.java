package ru.i_novus.is.integration.common.api;

import java.util.Date;

public class MonitoringModel {

    private String uid;
    private Date dateTime;
    private String sender;
    private String receiver;
    private String operation;
    private int status;
    private String error;

    public MonitoringModel(String uid, Date dateTime, String sender, String receiver, String operation, int status) {
        this.uid = uid;
        this.dateTime = dateTime;
        this.sender = sender;
        this.receiver = receiver;
        this.operation = operation;
        this.status = status;
    }

    public MonitoringModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
