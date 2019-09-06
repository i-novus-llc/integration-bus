package ru.i_novus.integration.registry.backend.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("Участники")
public class Participant implements Serializable {

    @ApiModelProperty(value = "Код участника")
    private String code;

    @ApiModelProperty(value = "Код группы")
    private String groupCode;

    @ApiModelProperty(value = "Имя участника")
    private String name;

    @ApiModelProperty(value = "Приостановлен")
    private Boolean disable;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
}
