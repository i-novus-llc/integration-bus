package ru.i_novus.integration.model;

import java.io.Serializable;

public class InternalRequestModel extends AbstractRequestModel implements Serializable {
    private static final long serialVersionUID = 1370434225663592290L;

    private DataModel dataModel;

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }
}
