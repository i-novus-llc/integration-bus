package ru.i_novus.integration.registry.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(schema = "integration", name = "participant_group")
public class ParticipantGroupEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

}
