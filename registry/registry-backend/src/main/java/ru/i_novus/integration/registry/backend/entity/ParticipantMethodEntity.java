package ru.i_novus.integration.registry.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(schema = "integration", name = "participant_method")
public class ParticipantMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "integration.participant_method_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "participant_code")
    private String participantCode;

    @Column(name = "method_code")
    private String methodCode;

    @Column(name = "url")
    private String url;

    @Column(name = "disable")
    private Boolean disable;

    @Column(name = "integration_type")
    private String integrationType;

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "sync")
    private boolean sync;

}
