package ru.i_novus.integration.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MonitoringModel implements Serializable {

    private String uid;
    private LocalDateTime dateTime;
    private String sender;
    private String receiver;
    private String operation;
    private int status;
    private String error;

    public MonitoringModel(String uid, LocalDateTime dateTime, String sender, String receiver, String operation, int status, String error) {
        this.uid = uid;
        this.dateTime = dateTime;
        this.sender = sender;
        this.receiver = receiver;
        this.operation = operation;
        this.status = status;
        this.error = error;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
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
