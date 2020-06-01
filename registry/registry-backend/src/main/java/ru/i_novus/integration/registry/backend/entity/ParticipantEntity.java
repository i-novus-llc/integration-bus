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
@Table(schema = "integration", name = "participant")
public class ParticipantEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "name")
    private String name;

    @Column(name = "disable")
    private Boolean disable;

    @Column(name = "has_auth")
    private Boolean hasAuth;

}
