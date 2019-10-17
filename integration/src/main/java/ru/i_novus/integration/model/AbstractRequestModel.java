package ru.i_novus.integration.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public abstract class AbstractRequestModel {
    private String uid;
    private List<String> recipient;
    private String method;
    private String envCode;

}
