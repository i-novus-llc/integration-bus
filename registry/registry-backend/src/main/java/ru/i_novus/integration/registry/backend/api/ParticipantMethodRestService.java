package ru.i_novus.integration.registry.backend.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.data.domain.Page;
import ru.i_novus.integration.registry.backend.criteria.ParticipantMethodCriteria;
import ru.i_novus.integration.registry.backend.model.IntegrationType;
import ru.i_novus.integration.registry.backend.model.ParticipantMethod;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST сервис управления участниками
 */
@Path("/participantMethods")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api("Сервисы систем участниц")
public interface ParticipantMethodRestService {

    @GET
    @Path("/")
    @ApiOperation("Найти все сервисы системы")
    @ApiResponse(code = 200, message = "Страница сервисов")
    Page<ParticipantMethod> findAll(@BeanParam ParticipantMethodCriteria criteria);

    @GET
    @Path("/{id}")
    @ApiOperation("Получить сервис по идентификатору")
    @ApiResponse(code = 200, message = "Сервис системы")
    ParticipantMethod getById(@ApiParam(value = "Идентификатор") @PathParam("id") Integer id);

    @POST
    @Path("/")
    @ApiOperation("Создать сервис системы")
    @ApiResponse(code = 200, message = "Созданный сервис")
    ParticipantMethod create(@ApiParam(value = "Сервис системы") ParticipantMethod participantMethod);

    @PUT
    @Path("/")
    @ApiOperation("Изменить сервис системы")
    @ApiResponse(code = 200, message = "Измененный сервис")
    ParticipantMethod update(@ApiParam(value = "Сервис системы") ParticipantMethod participantMethod);

    @DELETE
    @Path("/{id}")
    @ApiOperation("Удалить сервис")
    @ApiResponse(code = 200, message = "Сервис удален")
    void delete(@ApiParam(value = "Идентификатор") @PathParam("id") Integer id);

    @GET
    @Path("/integrationTypes")
    @ApiOperation("Получить все типы взаимодействия")
    @ApiResponse(code = 200, message = "Тип взаимодействия")
    List<IntegrationType> getAllIntegrationTypes();

}
