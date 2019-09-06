package ru.i_novus.integration.registry.backend.criteria;

import io.swagger.annotations.ApiParam;
import net.n2oapp.platform.jaxrs.RestCriteria;

import javax.ws.rs.QueryParam;

public class ParticipantCriteria extends RestCriteria {

    @QueryParam("code")
    @ApiParam(value = "Код участника")
    private String code;

    @QueryParam("name")
    @ApiParam(value = "Имя участника")
    private String name;

    @QueryParam("disableSelectId")
    @ApiParam(value = "Приостановить")
    private Integer disableSelectId;

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

    public Integer getDisableSelectId() {
        return disableSelectId;
    }

    public void setDisableSelectId(Integer disableSelectId) {
        this.disableSelectId = disableSelectId;
    }
}
