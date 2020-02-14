package ru.i_novus.integration.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.common.api.model.ParticipantModel;
import ru.i_novus.integration.configuration.IntegrationProperties;
import ru.i_novus.integration.model.*;
import ru.i_novus.integration.rest.client.RegistryClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class CommonModelPrepareService {

    private final RegistryClient registryClient;
    private final IntegrationProperties property;
    private final MessageSource messageSource;

    public CommonModelPrepareService(RegistryClient registryClient, IntegrationProperties property, MessageSource messageSource) {
        this.registryClient = registryClient;
        this.property = property;
        this.messageSource = messageSource;
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
                participantModel.setReceiver(recipient);
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
        commonModel.setMonitoringModel(prepareModel(model, recipient, participantModel.getMethod()));

        return commonModel;
    }

    private MonitoringModel prepareModel(Object values, String recipient, String method) {
        MonitoringModel monitoringModel = null;
        String envCode;
        if (values instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> map = (Map<String, String>) values;
            envCode = map.get("envCode") != null ? map.get("envCode") : property.getEnvCode();
            monitoringModel = new MonitoringModel(UUID.randomUUID().toString(), LocalDateTime.now(), envCode,
                    recipient, method, MessageStatusEnum.CREATE.getId());
        }
        if (values instanceof InternalRequestModel) {
            InternalRequestModel model = (InternalRequestModel) values;
            envCode = model.getEnvCode() != null ? model.getEnvCode() : property.getEnvCode();
            monitoringModel = new MonitoringModel(model.getUid(),
                    LocalDateTime.now(), envCode, recipient, method, MessageStatusEnum.CREATE.getId());

            monitoringModel.setComment(messageSource.getMessage("send.file.operation", null, Locale.ENGLISH) +
                    model.getDataModel().getName());
        }
        if (values instanceof RequestModel) {
            RequestModel model = (RequestModel) values;
            envCode = model.getEnvCode() != null ? model.getEnvCode() : property.getEnvCode();
            monitoringModel = new MonitoringModel(model.getUid(),
                    LocalDateTime.now(), envCode, recipient, method, MessageStatusEnum.CREATE.getId());
        }
        return monitoringModel;
    }

}
