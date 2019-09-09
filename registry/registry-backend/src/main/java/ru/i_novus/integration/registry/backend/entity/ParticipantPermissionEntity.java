package ru.i_novus.integration.registry.backend.entity;

import javax.persistence.*;

@Entity
@Table(schema = "integration", name = "participant_permission")
public class ParticipantPermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "integration.participant_permission_id_seq",
            allocationSize = 1)
    private int id;

    @Column(name = "participant_method_id")
    private Integer participantMethodId;

    @Column(name = "participant_code")
    private String participantCode;

    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "callback_url")
    private String callBackUrl;

    @Column(name = "sync")
    private boolean sync;

}
