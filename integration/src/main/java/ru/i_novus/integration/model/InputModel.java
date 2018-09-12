package ru.i_novus.integration.model;

import java.io.Serializable;
import java.util.List;

public class InputModel implements Serializable {
    private String uid;
    private String recipient;
    private List<DataModel> dataModels;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public List<DataModel> getDataModels() {
        return dataModels;
    }

    public void setDataModels(List<DataModel> dataModels) {
        this.dataModels = dataModels;
    }

}
