package ru.i_novus.integration.registry.backend.criteria;

import io.swagger.annotations.ApiParam;
import net.n2oapp.platform.jaxrs.RestCriteria;

import javax.ws.rs.QueryParam;

public class ParticipantMethodCriteria extends RestCriteria {

    @QueryParam("participantCode")
    @ApiParam(value = "Код системы участницы")
    private String participantCode;

    public String getParticipantCode() {
        return participantCode;
    }

    public void setParticipantCode(String participantCode) {
        this.participantCode = participantCode;
    }
}
