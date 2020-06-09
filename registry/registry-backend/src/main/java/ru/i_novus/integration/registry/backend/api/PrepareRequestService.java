package ru.i_novus.integration.registry.backend.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import ru.i_novus.integration.common.api.model.ParticipantModel;
import ru.i_novus.integration.common.api.model.RegistryInfoModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST сервис подготовки request
 */
@Path("/service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api("Подготовка request")
public interface PrepareRequestService {

    @POST
    @Path("/prepareRequest")
    @ApiOperation("Проверка прав")
    @ApiResponse(code = 200, message = "request подготовлен")
    ParticipantModel getServiceInfo(@ApiParam RegistryInfoModel model);

    @POST
    @Path("/checkAuthorization")
    @ApiOperation("Проверка авторизации сервиса")
    @ApiResponse(code = 200, message = "Авторизован")
    Response checkAuthorization(RegistryInfoModel model);
}
