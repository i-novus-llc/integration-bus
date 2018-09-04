package ru.i_novus.integration.model;

import java.io.Serializable;

public class CommonModel<T> implements Serializable {
    private MonitoringModel monitoringModel;
    private T object;

    public MonitoringModel getMonitoringModel() {
        return monitoringModel;
    }

    public void setMonitoringModel(MonitoringModel monitoringModel) {
        this.monitoringModel = monitoringModel;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
