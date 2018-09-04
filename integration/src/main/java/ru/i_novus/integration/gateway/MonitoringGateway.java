package ru.i_novus.integration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.messaging.Message;

public interface MonitoringGateway {

    @Gateway(requestChannel = "monitoringJmsInChannel")
    void putToQueue(Message model);
}
