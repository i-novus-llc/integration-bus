package ru.i_novus.integration.model;

import java.io.Serializable;

public class InputModel implements Serializable {
    private String recipient;
    private String path;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
