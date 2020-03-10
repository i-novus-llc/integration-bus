package ru.i_novus.integration.monitoring.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.i_novus.integration.monitoring.backend.model.SentMessageModel;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(schema = "monitoring", name = "sent_message")
public class SentMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "monitoring.sent_message_id_seq",
            allocationSize = 1)
    private Integer id;

    @Column(name = "uid", nullable = false)
    private String uid;

    @Column(name = "sent_date_time", nullable = false)
    private LocalDateTime sentDateTime;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "receiver", nullable = false)
    private String receiver;

    @Column(name = "operation", nullable = false)
    private String operation;

    @Column(name = "current_status", nullable = false)
    private String currentStatus;

    @Column(name = "comment")
    private String comment;

    public SentMessageEntity(String uid, LocalDateTime sentDateTime, String sender, String receiver, String operation,
                             String status, String comment) {
        this.uid = uid;
        this.sentDateTime = sentDateTime;
        this.sender = sender;
        this.receiver = receiver;
        this.operation = operation;
        this.currentStatus = status;
        this.comment = comment;
    }

    public SentMessageModel getSentMessageModel() {
        SentMessageModel model = new SentMessageModel();
        model.setId(this.id);
        model.setUid(this.uid);
        model.setSender(this.sender);
        model.setSentDateTime(this.sentDateTime);
        model.setReceiver(this.receiver);
        model.setOperation(this.operation);
        model.setComment(this.comment);
        switch (this.currentStatus) {
            case ("CREATE"):
                model.setStatus("Создан");
                break;
            case ("QUEUE"):
                model.setStatus("В очереди");
                break;
            case ("ERROR"):
                model.setStatus("Ошибка");
                break;
            case ("SUCCES"):
                model.setStatus("Удачно");
                break;
        }


        return model;
    }
}
