package ru.i_novus.integration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.messaging.Message;
import ru.i_novus.integration.model.CommonModel;

public interface InboundGateway {
    @Gateway(requestChannel = "requestChannel", replyChannel = "responseChannel")
    Message syncRequest(CommonModel model);

    @Gateway(requestChannel = "requestAsyncChannel")
    void aSyncRequest(CommonModel model);
}
