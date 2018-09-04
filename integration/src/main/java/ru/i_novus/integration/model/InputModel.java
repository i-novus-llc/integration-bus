package ru.i_novus.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InputModel implements Serializable {
    private String recipient;
    private String sender;
    private String host;
    private String status;
    private List<DataModel> dataModels;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataModel> getDataModels() {
        return dataModels;
    }

    public void setDataModels(List<DataModel> dataModels) {
        this.dataModels = dataModels;
    }

}
