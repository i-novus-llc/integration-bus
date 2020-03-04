package ru.i_novus.integration.common.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistryInfoModel {
    private String sender;
    private String receiver;
    private String method;
}
