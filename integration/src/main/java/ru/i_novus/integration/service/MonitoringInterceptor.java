package ru.i_novus.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.MonitoringHeaderModel;

@Component
public class MonitoringInterceptor implements ChannelInterceptor {

    @Autowired
    MonitoringGateway monitoringGateway;

    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        MonitoringHeaderModel monitoringHeader= (MonitoringHeaderModel) message.getHeaders();
        monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringHeader.fillMonitoringModel()).build());

        return message;
    }

}
