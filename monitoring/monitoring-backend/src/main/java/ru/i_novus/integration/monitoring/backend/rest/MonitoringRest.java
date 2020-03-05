package ru.i_novus.integration.monitoring.backend.rest;

import org.springframework.data.domain.Page;
import ru.i_novus.integration.monitoring.backend.criteria.SentMessageCriteria;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.backend.criteria.SentMessageStageCriteria;
import ru.i_novus.integration.monitoring.backend.model.ErrorModel;
import ru.i_novus.integration.monitoring.backend.model.SentMessageModel;
import ru.i_novus.integration.monitoring.backend.model.SentMessageStageModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface MonitoringRest {
    @GET
    @Path("/")
    Page<SentMessageModel> findAll(@BeanParam SentMessageCriteria criteria);

    @POST
    @Path(("/save"))
    void getServiceInfo(@BeanParam MonitoringModel model);

    @GET
    @Path("/stage")
    Page<SentMessageStageModel> findByUid(@BeanParam SentMessageStageCriteria criteria);

    @GET
    @Path("error/{id}")
    ErrorModel getErrorStackTrace(@PathParam("id") Integer id);
}
