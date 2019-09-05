package ru.i_novus.is.integration.common.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class MonitoringModel implements Serializable {

    private String uid;
    private Date dateTime;
    private String sender;
    private String receiver;
    private String operation;
    private int status;
    private String error;
    private String comment;

    public MonitoringModel(String uid, Date dateTime, String sender, String receiver, String operation, int status) {
        this.uid = uid;
        this.dateTime = dateTime;
        this.sender = sender;
        this.receiver = receiver;
        this.operation = operation;
        this.status = status;
    }
}
