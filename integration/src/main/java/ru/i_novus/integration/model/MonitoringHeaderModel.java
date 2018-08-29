package ru.i_novus.integration.model;

import org.springframework.messaging.MessageHeaders;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class MonitoringHeaderModel extends MessageHeaders {

    private String uid;
    private LocalDateTime dateTime;
    private String sender;
    private String receiver;
    private String operation;
    private int status;
    private String error;

    public MonitoringHeaderModel(Map<String, Object> headers, String uid, String receiver, LocalDateTime dateTime, String sender, int status) {
        super(headers);
        this.uid = uid;
        this.receiver = receiver;
        this.dateTime = dateTime;
        this.sender = sender;
        this.status = status;
    }

    public MonitoringHeaderModel(Map<String, Object> headers) {
        super(headers);
    }

    protected MonitoringHeaderModel(Map<String, Object> headers, UUID id, Long timestamp) {
        super(headers, id, timestamp);
    }

    public MonitoringModel fillMonitoringModel() {
        return new MonitoringModel(uid, dateTime, sender, receiver, operation, status, error);
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
