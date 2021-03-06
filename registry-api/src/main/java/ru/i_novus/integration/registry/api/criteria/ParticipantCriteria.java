package ru.i_novus.integration.registry.api.criteria;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.platform.jaxrs.RestCriteria;
import org.springframework.data.domain.Sort;

import javax.ws.rs.QueryParam;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
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

    @QueryParam("excludeParticipantMethodId")
    @ApiParam(value = "Код сервиса системы участницы, которую надо не включать в выборку")
    private Integer excludeParticipantMethodId;

    @Override
    protected List<Sort.Order> getDefaultOrders() {
        return Collections.singletonList(Sort.Order.asc("code"));
    }
}
