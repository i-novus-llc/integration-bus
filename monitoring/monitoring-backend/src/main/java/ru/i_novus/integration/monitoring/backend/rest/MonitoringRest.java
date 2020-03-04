package ru.i_novus.integration.monitoring.backend.rest;

import org.springframework.data.domain.Page;
import ru.i_novus.integration.monitoring.backend.MonitoringCriteria;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.backend.model.MonitoringFormModel;
import ru.i_novus.integration.monitoring.backend.model.MonitoringStageModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface MonitoringRest {
    @GET
    @Path("/")
    Page<MonitoringModel> findAll(@BeanParam MonitoringCriteria criteria);

    @GET
    @Path("/form")
    Page<MonitoringStageModel> findByUid(@BeanParam MonitoringCriteria criteria);

    @GET
    @Path("/header")
    MonitoringFormModel fillHeader(@QueryParam("uid") String uid, @QueryParam("sender") String sender,
                                  @QueryParam("receiver") String receiver, @QueryParam("operation") String operation);

    @GET
    @Path("error/{id}")
    String getErrorStackTrace(@PathParam("id") Integer id);
}
