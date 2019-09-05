package ru.i_novus.integration.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DataModel implements Serializable {
    private String path;
    private String name;
    private String mime;
    private DataModel attachment;
}
