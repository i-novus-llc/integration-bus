package ru.i_novus.integration.registry.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "integration", name = "integration_type")
public class IntegrationTypeEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
