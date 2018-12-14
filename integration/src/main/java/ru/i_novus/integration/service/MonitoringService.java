package ru.i_novus.integration.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.InputModel;
import ru.i_novus.integration.model.MessageStatusEnum;
import ru.i_novus.is.integration.common.api.MonitoringModel;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Component
public class MonitoringService {

    @Autowired
    MonitoringGateway monitoringGateway;

    @Autowired
    PlaceholdersProperty property;

    @Autowired
    MessageSource messageSource;


    public Message<CommonModel> create(@Payload CommonModel commonModel, int status) {
        MonitoringModel monitoringModel = commonModel.getMonitoringModel();
        monitoringModel.setStatus(status);
        monitoringModel.setDateTime(new Date());
        monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringModel).build());

        return MessageBuilder.withPayload(commonModel).build();
    }

    public void fineStatus(@Payload CommonModel commonModel) {
        MonitoringModel monitoringModel = commonModel.getMonitoringModel();
        monitoringModel.setStatus(MessageStatusEnum.SEND.getId());
        monitoringModel.setDateTime(new Date());
        monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringModel).build());
    }

    public void createError(Message message) {
        MonitoringModel model;
        if (message.getPayload() instanceof MessageHandlingException) {
            MessageHandlingException exceptionMessage = (MessageHandlingException) message.getPayload();
            CommonModel commonModel = (CommonModel) exceptionMessage.getFailedMessage().getPayload();
            model = commonModel.getMonitoringModel();
            model.setStatus(MessageStatusEnum.ERROR.getId());
            model.setDateTime(new Date());
            model.setError(exceptionMessage.getMessage() + " StackTrace: " + ExceptionUtils.getStackTrace(exceptionMessage));
        } else {
            model = (MonitoringModel) message.getPayload();
            model.setStatus(MessageStatusEnum.ERROR.getId());
            model.setDateTime(new Date());
        }

        monitoringGateway.putToQueue(MessageBuilder.withPayload(model).build());
    }

    public MonitoringModel prepareModel(Object values) {
        MonitoringModel monitoringModel = null;
        if (values instanceof Map) {
            Map<String, String> map = (Map<String, String>) values;
            monitoringModel = new MonitoringModel(UUID.randomUUID().toString(), new Date(), property.getEnvCode(),
                    map.get("recipient"), map.get("method"), MessageStatusEnum.CREATE.getId());
        }
        if (values instanceof InputModel) {
            InputModel model = (InputModel) values;

            monitoringModel = new MonitoringModel(UUID.randomUUID().toString(),
                    new Date(), property.getEnvCode(), model.getRecipient(), model.getMethod(), MessageStatusEnum.CREATE.getId());

            monitoringModel.setComment(messageSource.getMessage("send.file.operation", null, Locale.ENGLISH) +
                    model.getDataModel().getName());
        }
        return monitoringModel;
    }

}
