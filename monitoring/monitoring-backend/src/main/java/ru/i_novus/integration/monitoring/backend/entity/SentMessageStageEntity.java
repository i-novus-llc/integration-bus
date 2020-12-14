package ru.i_novus.integration.monitoring.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.i_novus.integration.monitoring.api.model.SentMessageStageModel;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(schema = "monitoring", name = "sent_message_stage")
public class SentMessageStageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "monitoring.sent_message_stage_id_seq",
            allocationSize = 1)
    private Integer id;

    @Column(name = "sent_message_id", nullable = false)
    private Integer sentMessageId;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "error")
    private String error;


    public SentMessageStageEntity(Integer sentMessageId, LocalDateTime dateTime, String status, String error, String comment) {
        this.sentMessageId = sentMessageId;
        this.dateTime = dateTime;
        this.status = status;
        this.error = error;
    }

    public SentMessageStageModel fillSentMessageStageModel() {
        SentMessageStageModel model = new SentMessageStageModel();
        model.setId(this.id);
        model.setDateTime(this.dateTime);
        model.setError(this.error != null ? this.error.substring(0, 20) + "..." : "");
        switch (this.status) {
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
