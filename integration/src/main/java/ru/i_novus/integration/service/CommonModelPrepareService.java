package ru.i_novus.integration.service;

import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.model.AbstractRequestModel;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.RequestModel;
import ru.i_novus.integration.rest.client.RegistryClient;
import ru.i_novus.integration.common.api.ParticipantModel;

import java.io.IOException;
import java.util.Map;

@Component
public class CommonModelPrepareService {

    private final RegistryClient registryClient;
    private final PlaceholdersProperty property;
    private final MonitoringService monitoringService;

    public CommonModelPrepareService(RegistryClient registryClient, PlaceholdersProperty property, MonitoringService monitoringService) {
        this.registryClient = registryClient;
        this.property = property;
        this.monitoringService = monitoringService;
    }

    public CommonModel getRequestModelPrepare(Map<String, String> requestParams, String method, String service) throws IOException {

        String envCode = requestParams.containsKey("envCode") ? requestParams.get("envCode") : property.getEnvCode();
        ParticipantModel participantModel = registryClient.getServiceParticipant(service, envCode, method);

        return prepareCommonModel(participantModel, requestParams);
    }

    public CommonModel requestModelPreparation(AbstractRequestModel requestModel) throws IOException {
        String envCode = requestModel.getEnvCode() != null ? requestModel.getEnvCode() : property.getEnvCode();
        ParticipantModel participantModel = registryClient.getServiceParticipant(requestModel.getRecipient(), envCode, requestModel.getMethod());

        CommonModel commonModel =  prepareCommonModel(participantModel, requestModel);

        if (requestModel instanceof RequestModel) {
            commonModel.setObject(((RequestModel) requestModel).getMessage());
        }

        return commonModel;
    }

    private CommonModel prepareCommonModel(ParticipantModel participantModel, Object model) {
        CommonModel commonModel = new CommonModel();
        commonModel.setParticipantModel(participantModel);
        commonModel.setMonitoringModel(monitoringService.prepareModel(model));

        return commonModel;
    }

}
