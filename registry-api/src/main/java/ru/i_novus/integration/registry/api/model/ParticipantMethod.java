package ru.i_novus.integration.registry.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Сервисы систем участников")
public class ParticipantMethod {

    @ApiModelProperty(value = "Идентификатор сервиса")
    private Integer id;

    @ApiModelProperty(value = "Код системы участницы")
    private String participantCode;

    @ApiModelProperty(value = "Метод сервиса")
    private String methodCode;

    @ApiModelProperty(value = "URL сервиса")
    private String url;

    @ApiModelProperty(value = "Приостановлен")
    private Boolean disable;

    @ApiModelProperty(value = "Тип взаимодействия")
    private IntegrationType integrationType;

    @ApiModelProperty(value = "URL возврата")
    private String callbackUrl;

    @ApiModelProperty(value = "Синхронно")
    private boolean sync;

}
