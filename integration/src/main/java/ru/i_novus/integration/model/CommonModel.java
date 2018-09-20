package ru.i_novus.integration.model;

import ru.i_novus.is.integration.common.api.MonitoringModel;
import ru.i_novus.is.integration.common.api.ParticipantModel;

import java.io.Serializable;

public class CommonModel<T> implements Serializable {
    private MonitoringModel monitoringModel;
    private ParticipantModel participantModel;
    private T object;

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

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
