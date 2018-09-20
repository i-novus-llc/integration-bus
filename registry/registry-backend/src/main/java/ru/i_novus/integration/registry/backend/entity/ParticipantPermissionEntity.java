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
    private String callbackUrl;

    @Column(name = "sync")
    private boolean sync;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getParticipantMethodId() {
        return participantMethodId;
    }

    public void setParticipantMethodId(Integer participantMethodId) {
        this.participantMethodId = participantMethodId;
    }

    public String getParticipantCode() {
        return participantCode;
    }

    public void setParticipantCode(String participantCode) {
        this.participantCode = participantCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
