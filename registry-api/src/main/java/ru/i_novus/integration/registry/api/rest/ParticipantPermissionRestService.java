package ru.i_novus.integration.registry.api.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.data.domain.Page;
import ru.i_novus.integration.registry.api.criteria.ParticipantPermissionCriteria;
import ru.i_novus.integration.registry.api.model.ParticipantPermission;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * REST сервис управления участниками
 */
@Path("/participantPermissions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api("Права доступа к сервисам")
public interface ParticipantPermissionRestService {
    @GET
    @Path("/")
    @ApiOperation("Найти все права доступа к сервисам")
    @ApiResponse(code = 200, message = "Страница прав доступа к сервисам")
    Page<ParticipantPermission> findAll(@BeanParam ParticipantPermissionCriteria criteria);

    @GET
    @Path("/{id}")
    @ApiOperation("Получить право доступа к сервисам по идентификатору")
    @ApiResponse(code = 200, message = "Право доступа к сервисам")
    ParticipantPermission getById(@ApiParam(value = "Идентификатор") @PathParam("id") Integer id);


    @POST
    @Path("/")
    @ApiOperation("Создать Право доступа к сервисам")
    @ApiResponse(code = 200, message = "Созданное право доступа к сервисам")
    ParticipantPermission create(@ApiParam(value = "Право доступа к сервисам") ParticipantPermission ParticipantPermission);

    @PUT
    @Path("/")
    @ApiOperation("Изменить право доступа к сервисам")
    @ApiResponse(code = 200, message = "Измененное право доступа к сервисам")
    ParticipantPermission update(@ApiParam(value = "Право доступа к сервисам") ParticipantPermission ParticipantPermission);

    @DELETE
    @Path("/{id}")
    @ApiOperation("Удалить право доступа к сервисам")
    @ApiResponse(code = 200, message = "Право доступа к сервисам удалено")
    void delete(@ApiParam(value = "Идентификатор") @PathParam("id") Integer id);

}
