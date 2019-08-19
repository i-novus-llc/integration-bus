package ru.i_novus.integration.model;

import java.io.Serializable;

public class RequestModel extends AbstractRequestModel implements Serializable {
    private static final long serialVersionUID = 2038228447534999790L;

    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
