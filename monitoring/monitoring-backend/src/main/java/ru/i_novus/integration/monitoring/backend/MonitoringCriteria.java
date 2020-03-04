package ru.i_novus.integration.monitoring.backend;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.n2oapp.platform.jaxrs.RestCriteria;

import javax.ws.rs.QueryParam;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MonitoringCriteria extends RestCriteria {

    @QueryParam("uid")
    private String uid;
    @QueryParam("dateFrom")
    private LocalDateTime dateFrom;
    @QueryParam("dateTo")
    private LocalDateTime dateTo;
    @QueryParam("sender")
    private String sender;
    @QueryParam("receiver")
    private String receiver;
    @QueryParam("status")
    private Integer status;
    @QueryParam("comment")
    private String comment;
    @QueryParam("operation")
    private String operation;
}
