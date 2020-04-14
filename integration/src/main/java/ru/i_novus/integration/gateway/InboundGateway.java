package ru.i_novus.integration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import ru.i_novus.integration.common.api.MonitoringModel;
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
    void preparation(CommonModel modelMessage);

    @Gateway(requestChannel = "senderQueueChannel")
    void sender(CommonModel modelMessage);

    @Gateway(requestChannel = "asyncQueueChannel")
    void async(CommonModel modelMessage);

    @Gateway(requestChannel = "monitoringQueueChannel")
    void monitoring(MonitoringModel model);

}
