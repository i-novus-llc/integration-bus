package ru.i_novus.integration.common.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringModel implements Serializable {

    private static final long serialVersionUID = -7797168922682390306L;
    private Integer id;
    private String uid;
    private LocalDateTime dateTime;
    private String sender;
    private String receiver;
    private String operation;
    private String status;
    private String error;
    private String comment;

    public MonitoringModel(String uid, LocalDateTime dateTime, String sender, String receiver, String operation, String status) {
        this.uid = uid;
        this.dateTime = dateTime;
        this.sender = sender;
        this.receiver = receiver;
        this.operation = operation;
        this.status = status;
    }
}
