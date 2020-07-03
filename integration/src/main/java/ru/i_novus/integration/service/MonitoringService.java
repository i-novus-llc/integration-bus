package ru.i_novus.integration.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.MessageStatusEnum;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Component
@EnableAsync
public class MonitoringService {

    private final MonitoringGateway monitoringGateway;

    @Autowired
    public MonitoringService(MonitoringGateway monitoringGateway) {
        this.monitoringGateway = monitoringGateway;
    }

    @SuppressWarnings("unused")
    public Message<CommonModel> create(@Payload CommonModel commonModel, String status,
                                       @Headers Map<String, Object> headers) {
        MonitoringModel monitoringModel = commonModel.getMonitoringModel();
        if (!monitoringModel.getReceiver().equals("nsi")) {
            monitoringModel.setStatus(status);
            monitoringModel.setDateTime(LocalDateTime.now());
            monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringModel).build());
        }
        return MessageBuilder.createMessage(commonModel, new MessageHeaders(headers));
    }

    @Async
    public void fineStatus(@Payload CommonModel commonModel) {
        MonitoringModel monitoringModel = commonModel.getMonitoringModel();
        monitoringModel.setStatus(MessageStatusEnum.SEND.getId());
        monitoringModel.setDateTime(LocalDateTime.now());
        monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringModel).build());
    }

    @Async
    public void createError(Message message) {
        MonitoringModel model;
        if (message.getPayload() instanceof MessageHandlingException) {
            MessageHandlingException exceptionMessage = (MessageHandlingException) message.getPayload();
            Object payload = Objects.requireNonNull(exceptionMessage.getFailedMessage()).getPayload();
            if (payload instanceof CommonModel) {
                CommonModel commonModel = (CommonModel) payload;
                model = commonModel.getMonitoringModel();
            } else {
                model = (MonitoringModel) payload;
            }
            model.setStatus(MessageStatusEnum.ERROR.getId());
            model.setDateTime(LocalDateTime.now());
            model.setError(exceptionMessage.getMessage() + " StackTrace: " + ExceptionUtils.getStackTrace(exceptionMessage));
        } else {
            model = (MonitoringModel) message.getPayload();
            model.setStatus(MessageStatusEnum.ERROR.getId());
            model.setDateTime(LocalDateTime.now());
        }
        monitoringGateway.putToQueue(MessageBuilder.withPayload(model).build());
    }
}
