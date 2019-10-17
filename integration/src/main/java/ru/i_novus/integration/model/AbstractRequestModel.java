package ru.i_novus.integration.model;

import java.util.List;

public abstract class AbstractRequestModel {
    private String uid;
    private List<String> recipient;
    private String method;
    private String envCode;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getRecipient() {
        return recipient;
    }

    public void setRecipient(List<String> recipient) {
        this.recipient = recipient;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }
}
