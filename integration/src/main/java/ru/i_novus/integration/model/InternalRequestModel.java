package ru.i_novus.integration.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InternalRequestModel implements Serializable {
    private static final long serialVersionUID = 1370434225663592290L;
    private String uid;
    private String recipient;
    private String method;
    private String envCode;
    private DataModel dataModel;

}
