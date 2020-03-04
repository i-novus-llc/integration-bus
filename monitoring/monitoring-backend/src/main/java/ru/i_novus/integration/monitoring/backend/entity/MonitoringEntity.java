package ru.i_novus.integration.monitoring.backend.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.backend.model.MonitoringStageModel;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
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
    private LocalDateTime dateTime;

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

    @Column(name = "comment")
    private String comment;

    public MonitoringModel getMonitoringModel() {
        MonitoringModel model = new MonitoringModel();
        model.setId(this.getId());
        model.setUid(this.getUid());
        model.setSender(this.getSender());
        model.setReceiver(this.getReceiver());
        model.setStatus(this.getStatus());
        model.setOperation(this.getOperation());
        model.setDateTime(this.getDateTime());
        model.setComment(this.getComment());
        return model;
    }

    public MonitoringStageModel fillMonitoringStageModel() {
        MonitoringStageModel stageModel = new MonitoringStageModel();
        stageModel.setId(this.getId());
        stageModel.setDateTime(this.getDateTime());
        stageModel.setStatus(this.getStatus());
        stageModel.setComment(this.getComment());
        stageModel.setError(this.getError().substring(0, 20) + "...");

        return stageModel;
    }
}
