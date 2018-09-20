package ru.i_novus.integration.registry.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
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


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
}