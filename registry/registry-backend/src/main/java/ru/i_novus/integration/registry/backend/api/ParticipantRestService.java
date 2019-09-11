package ru.i_novus.integration.registry.backend.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.data.domain.Page;
import ru.i_novus.integration.registry.backend.criteria.ParticipantCriteria;
import ru.i_novus.integration.registry.backend.model.Participant;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * REST сервис управления участниками
 */
@Path("/participants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api("Участники")
public interface ParticipantRestService {
    @GET
    @Path("/")
    @ApiOperation("Найти всех участников")
    @ApiResponse(code = 200, message = "Страница участников")
    Page<Participant> findAll(@BeanParam ParticipantCriteria criteria);

    @GET
    @Path("/{id}")
    @ApiOperation("Получить участника по идентификатору")
    @ApiResponse(code = 200, message = "Участник")
    Participant getById(@ApiParam(value = "Идентификатор") @PathParam("id") String id);


    @POST
    @Path("/")
    @ApiOperation("Создать участника")
    @ApiResponse(code = 200, message = "Созданный участник")
    Participant create(@ApiParam(value = "Участник") Participant participant);

    @PUT
    @Path("/")
    @ApiOperation("Изменить участника")
    @ApiResponse(code = 200, message = "Измененный участник")
    Participant update(@ApiParam(value = "Участник") Participant participant);

    @DELETE
    @Path("/{code}")
    @ApiOperation("Удалить участника")
    @ApiResponse(code = 200, message = "Участник удален")
    void delete(@ApiParam(value = "Идентификатор") @PathParam("code") String code);

}
