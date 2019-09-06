package ru.i_novus.integration.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.MessageHeaders;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
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

}
