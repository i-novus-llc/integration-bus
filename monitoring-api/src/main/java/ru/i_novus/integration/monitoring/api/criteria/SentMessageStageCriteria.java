package ru.i_novus.integration.monitoring.api.criteria;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.n2oapp.platform.jaxrs.RestCriteria;
import org.springframework.data.domain.Sort;

import javax.ws.rs.QueryParam;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SentMessageStageCriteria extends RestCriteria {

    @QueryParam("sentMessageId")
    private String sentMessageId;

    @Override
    protected List<Sort.Order> getDefaultOrders() {
        return Collections.emptyList();
    }
}
