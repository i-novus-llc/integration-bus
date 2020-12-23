package ru.i_novus.integration.monitoring.api.criteria;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.n2oapp.platform.jaxrs.RestCriteria;
import org.springframework.data.domain.Sort;

import javax.ws.rs.QueryParam;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SentMessageCriteria extends RestCriteria {

    @QueryParam("uid")
    private String uid;
    @QueryParam("sentDateTime")
    private LocalDateTime sentDateTime;
    @QueryParam("dateTo")
    private LocalDateTime dateTo;
    @QueryParam("sender")
    private String sender;
    @QueryParam("receiver")
    private String receiver;
    @QueryParam("status")
    private String status;
    @QueryParam("comment")
    private String comment;
    @QueryParam("operation")
    private String operation;

    @Override
    protected List<Sort.Order> getDefaultOrders() {
        return Collections.singletonList(Sort.Order.asc("sentDateTime"));
    }
}
