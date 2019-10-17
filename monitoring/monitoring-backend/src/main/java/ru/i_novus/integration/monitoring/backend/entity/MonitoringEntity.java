package ru.i_novus.integration.monitoring.backend.entity;


import ru.i_novus.integration.common.api.MonitoringModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @Column(name = "comment")
    private String comment;

    public MonitoringEntity(MonitoringModel model) {
        this.uid = model.getUid();
        this.dateTime = model.getDateTime();
        this.sender = model.getSender();
        this.receiver = model.getReceiver();
        this.operation = model.getOperation();
        this.status = Integer.toString(model.getStatus());
        this.error = model.getError();
        this.comment = model.getComment();
    }

}
