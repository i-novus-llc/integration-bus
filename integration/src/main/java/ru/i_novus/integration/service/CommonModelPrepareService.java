package ru.i_novus.integration.service;

import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.IntegrationProperties;
import ru.i_novus.integration.model.AbstractRequestModel;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.InternalRequestModel;
import ru.i_novus.integration.model.RequestModel;
import ru.i_novus.integration.rest.client.RegistryClient;
import ru.i_novus.integration.common.api.ParticipantModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CommonModelPrepareService {

    private final RegistryClient registryClient;
    private final IntegrationProperties property;
    private final MonitoringService monitoringService;

    public CommonModelPrepareService(RegistryClient registryClient, IntegrationProperties property, MonitoringService monitoringService) {
        this.registryClient = registryClient;
        this.property = property;
        this.monitoringService = monitoringService;
    }

    public CommonModel getRequestModelPrepare(Map<String, String> requestParams, String method, String service) throws IOException {

        String envCode = requestParams.containsKey("envCode") ? requestParams.get("envCode") : property.getEnvCode();
        ParticipantModel participantModel = registryClient.getServiceParticipant(service, envCode, method);

        return prepareCommonModel(participantModel, requestParams, service);
    }

    public List<CommonModel> requestModelPreparation(AbstractRequestModel requestModel) {
        List<CommonModel> commonModels = new ArrayList<>();
        String envCode = requestModel.getEnvCode() != null ? requestModel.getEnvCode() : property.getEnvCode();

        requestModel.getRecipient().forEach(recipient -> {
            ParticipantModel participantModel = null;
            try {
                participantModel = registryClient.getServiceParticipant(recipient, envCode, requestModel.getMethod());
            } catch (IOException e) {
                e.printStackTrace();
            }

            CommonModel commonModel = prepareCommonModel(participantModel, requestModel, recipient);

            if (requestModel instanceof RequestModel) {
                commonModel.setObject(((RequestModel) requestModel).getMessage());
            }

            commonModels.add(commonModel);
        });

        return commonModels;
    }

    public CommonModel requestModelPreparation(InternalRequestModel requestModel) throws IOException {
        String envCode = requestModel.getEnvCode() != null ? requestModel.getEnvCode() : property.getEnvCode();
        ParticipantModel participantModel = registryClient.getServiceParticipant(requestModel.getRecipient(), envCode, requestModel.getMethod());
        CommonModel commonModel = prepareCommonModel(participantModel, requestModel, requestModel.getRecipient());
        commonModel.setObject(requestModel);
        return commonModel;
    }

    private CommonModel prepareCommonModel(ParticipantModel participantModel, Object model, String recipient) {
        CommonModel commonModel = new CommonModel();
        commonModel.setParticipantModel(participantModel);
        commonModel.setMonitoringModel(monitoringService.prepareModel(model, recipient, participantModel.getMethod()));

        return commonModel;
    }

}
