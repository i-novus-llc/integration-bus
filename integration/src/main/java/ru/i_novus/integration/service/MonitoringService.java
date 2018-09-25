package ru.i_novus.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.MessageStatusEnum;
import ru.i_novus.is.integration.common.api.MonitoringModel;

import java.util.Date;

@Component
public class MonitoringService {

    @Autowired
    MonitoringGateway monitoringGateway;

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
        MonitoringModel model = (MonitoringModel) message.getPayload();
        model.setStatus(MessageStatusEnum.ERROR.getId());
        model.setDateTime(new Date());

        monitoringGateway.putToQueue(MessageBuilder.withPayload(model).build());
    }

    /*public void createError(MessageHandlingException message) {
        CommonModel commonModel = (CommonModel) message.getFailedMessage().getPayload();
        MonitoringModel monitoringModel = commonModel.getMonitoringModel();
        monitoringModel.setStatus(MessageStatusEnum.ERROR.getId());
        monitoringModel.setDateTime(new Date());
        monitoringModel.setError(message.getMessage());
        monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringModel).build());
    }*/
}
