package ru.i_novus.integration.registry.backend.entity;

import javax.persistence.*;

@Entity
@Table(schema = "integration", name = "participant_method")
public class ParticipantMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    @SequenceGenerator(name = "id_gen", sequenceName = "integration.participant_group_id_seq",
            allocationSize = 1)
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
    private String integration_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParticipantCode() {
        return participantCode;
    }

    public void setParticipantCode(String participantCode) {
        this.participantCode = participantCode;
    }

    public String getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getIntegration_type() {
        return integration_type;
    }

    public void setIntegration_type(String integration_type) {
        this.integration_type = integration_type;
    }
}