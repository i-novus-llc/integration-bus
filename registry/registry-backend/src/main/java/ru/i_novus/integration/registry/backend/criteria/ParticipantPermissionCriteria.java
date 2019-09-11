package ru.i_novus.integration.registry.backend.criteria;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.platform.jaxrs.RestCriteria;

import javax.ws.rs.QueryParam;

@Getter
@Setter
public class ParticipantPermissionCriteria extends RestCriteria {

    @QueryParam("participantMethodId")
    @ApiParam(value = "Код сервиса системы участницы")
    private Integer participantMethodId;

}
