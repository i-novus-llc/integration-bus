package ru.i_novus.integration.registry.backend.criteria;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.platform.jaxrs.RestCriteria;

import javax.ws.rs.QueryParam;

@Getter
@Setter
public class ParticipantMethodCriteria extends RestCriteria {

    @QueryParam("participantCode")
    @ApiParam(value = "Код системы участницы")
    private String participantCode;

    @QueryParam("methodCode")
    @ApiParam(value = "Метод сервиса")
    private String methodCode;

}
