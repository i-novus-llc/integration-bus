package ru.i_novus.integration.registry.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Права систем участников")
public class ParticipantPermission {

    @ApiModelProperty(value = "Идентификатор сервиса")
    private Integer id;

    @ApiModelProperty(value = "Код системы участницы")
    private String participantCode;

    @ApiModelProperty(value = "Метод сервиса")
    private Integer participantMethodId;

    @ApiModelProperty(value = "Код группы")
    private String groupCode;

    @ApiModelProperty(value = "URL возврата")
    private String callbackUrl;

    @ApiModelProperty(value = "Асинхронно")
    private boolean sync;
}
