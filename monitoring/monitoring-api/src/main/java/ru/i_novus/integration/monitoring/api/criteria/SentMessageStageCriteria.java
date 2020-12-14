package ru.i_novus.integration.monitoring.api.criteria;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.n2oapp.platform.jaxrs.RestCriteria;

import javax.ws.rs.QueryParam;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SentMessageStageCriteria extends RestCriteria {

    @QueryParam("sentMessageId")
    private String sentMessageId;
}
