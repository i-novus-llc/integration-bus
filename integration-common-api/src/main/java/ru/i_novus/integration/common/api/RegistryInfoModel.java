package ru.i_novus.integration.common.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistryInfoModel {
    private String sender;
    private String receiver;
    private String method;
}
