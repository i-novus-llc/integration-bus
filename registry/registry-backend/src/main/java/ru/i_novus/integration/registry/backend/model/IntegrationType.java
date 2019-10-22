package ru.i_novus.integration.registry.backend.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Тип взаимодействия")
public class IntegrationType {

    @ApiModelProperty(value = "Код тип взаимодействия")
    private String id;

    @ApiModelProperty(value = "Название типа взаимодействия")
    private String name;
}
