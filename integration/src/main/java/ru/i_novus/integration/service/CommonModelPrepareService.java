package ru.i_novus.integration.service;

import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.InputModel;
import ru.i_novus.integration.rest.client.RegistryClient;
import ru.i_novus.is.integration.common.api.ParticipantModel;

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

    public CommonModel syncRequestModelPrepare(Map<String, String> requestParams, String method) throws IOException {
        requestParams.put("method", method);
        String envCode = requestParams.containsKey("envCode") ? requestParams.get("envCode") : property.getEnvCode();
        ParticipantModel participantModel = registryClient.getServiceParticipant(requestParams.get("recipient"), envCode, method);

        return prepareCommonModel(participantModel, requestParams);
    }

    public CommonModel aSyncRequestModelPreparation(InputModel model) throws IOException {
        String envCode = model.getEnvCode() != null ? model.getEnvCode() : property.getEnvCode();
        ParticipantModel participantModel = registryClient.getServiceParticipant(model.getRecipient(), envCode, model.getMethod());

        return prepareCommonModel(participantModel, model);
    }

    private CommonModel prepareCommonModel(ParticipantModel participantModel, Object model) {
        CommonModel commonModel = new CommonModel();
        commonModel.setParticipantModel(participantModel);
        commonModel.setMonitoringModel(monitoringService.prepareModel(model));
        commonModel.setObject(model);

        return commonModel;
    }
}
