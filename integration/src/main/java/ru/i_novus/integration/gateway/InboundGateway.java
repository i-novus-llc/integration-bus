package ru.i_novus.integration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import ru.i_novus.integration.model.CommonModel;

@MessagingGateway
public interface InboundGateway {
    @Gateway(requestChannel = "requestSyncChannel", replyChannel = "responseSyncChannel")
    Message syncRequest(CommonModel model);

    @Gateway(requestChannel = "requestAsyncChannel")
    void aSyncRequest(CommonModel model);

    @Gateway(requestChannel = "internalRequestChannel")
    void internalRequest(CommonModel model);

    @Gateway(requestChannel = "preparationQueueChannel")
    void preparation(Message modelMessage);

    @Gateway(requestChannel = "senderQueueChannel")
    void sender(Message modelMessage);

    @Gateway(requestChannel = "asyncQueueChannel")
    void async(Message modelMessage);

    @Gateway(requestChannel = "monitoringQueueChannel")
    void monitoring(Message model);

}
