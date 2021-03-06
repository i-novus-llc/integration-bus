package ru.i_novus.integration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

import java.util.concurrent.Future;

@MessagingGateway
public interface MonitoringGateway {

    @Gateway(requestChannel = "monitoringJmsInChannel")
    void putToQueue(Message model);

    @Gateway(requestChannel = "errorChannel")
    Future createError(Message model);
}
