package ru.i_novus.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.messaging.Message;
import ru.i_novus.integration.model.InputModel;

public interface InboundGateway {
    @Gateway(requestChannel = "requestChannel", replyChannel = "responseChannel")
    Message syncRequest(Message message);

    @Gateway(requestChannel = "requestAsyncChannel")
    void aSyncRequest(InputModel model);
}
