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
public class ParticipantMethodCriteria extends RestCriteria {

    @QueryParam("participantCode")
    @ApiParam(value = "Код системы участницы")
    private String participantCode;

    @QueryParam("methodCode")
    @ApiParam(value = "Метод сервиса")
    private String methodCode;

    @Override
    protected List<Sort.Order> getDefaultOrders() {
        return Collections.singletonList(Sort.Order.asc("id"));
    }
}
