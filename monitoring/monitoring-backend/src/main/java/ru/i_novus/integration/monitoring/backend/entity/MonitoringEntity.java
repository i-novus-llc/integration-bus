package ru.i_novus.integration.monitoring.backend.entity;


import ru.i_novus.is.integration.common.api.MonitoringModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "monitoring", name = "monitoring")
public class MonitoringEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "monitoring.monitoring_id_seq",
            allocationSize = 1)
    private Integer id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "dateTime")
    private Date dateTime;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "operation")
    private String operation;

    @Column(name = "status")
    private String status;

    @Column(name = "error")
    private String error;

    public MonitoringEntity() {
    }

    public MonitoringEntity(MonitoringModel model) {
        this.uid = model.getUid();
        this.dateTime = model.getDateTime();
        this.sender = model.getSender();
        this.receiver = model.getReceiver();
        this.operation = model.getOperation();
        this.status = Integer.toString(model.getStatus());
        this.error = model.getError();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
