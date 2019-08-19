package ru.i_novus.integration.model;

import ru.i_novus.is.integration.common.api.MonitoringModel;
import ru.i_novus.is.integration.common.api.ParticipantModel;

import java.io.Serializable;

public class CommonModel implements Serializable {
    private static final long serialVersionUID = -6482737092304911196L;

    private MonitoringModel monitoringModel;
    private ParticipantModel participantModel;
    private Object object;

    public MonitoringModel getMonitoringModel() {
        return monitoringModel;
    }

    public void setMonitoringModel(MonitoringModel monitoringModel) {
        this.monitoringModel = monitoringModel;
    }

    public ParticipantModel getParticipantModel() {
        return participantModel;
    }

    public void setParticipantModel(ParticipantModel participantModel) {
        this.participantModel = participantModel;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
