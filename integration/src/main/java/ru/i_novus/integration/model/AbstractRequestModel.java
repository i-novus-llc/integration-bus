package ru.i_novus.integration.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class AbstractRequestModel{
    private String uid;
    private String recipient;
    private String method;
    private String envCode;

}
