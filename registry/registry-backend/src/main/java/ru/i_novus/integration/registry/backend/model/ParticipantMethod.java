package ru.i_novus.integration.registry.backend.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

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
    private String integrationType;

    @ApiModelProperty(value = "URL возврата")
    private String callbackUrl;

    @ApiModelProperty(value = "Синхронно")
    private boolean sync;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParticipantCode() {
        return participantCode;
    }

    public void setParticipantCode(String participantCode) {
        this.participantCode = participantCode;
    }

    public String getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(String integrationType) {
        this.integrationType = integrationType;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
