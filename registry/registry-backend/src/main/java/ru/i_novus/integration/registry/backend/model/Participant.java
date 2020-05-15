package ru.i_novus.integration.registry.backend.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("Системы - участницы")
public class Participant implements Serializable {

    private static final long serialVersionUID = -7210017035760441083L;

    @ApiModelProperty(value = "Код участника")
    private String code;

    @ApiModelProperty(value = "Код группы")
    private String groupCode;

    @ApiModelProperty(value = "Имя участника")
    private String name;

    @ApiModelProperty(value = "Приостановлен")
    private Boolean disable;

}
