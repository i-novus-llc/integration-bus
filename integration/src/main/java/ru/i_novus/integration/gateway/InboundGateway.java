package ru.i_novus.integration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.model.CommonModel;

import java.util.Map;

@MessagingGateway
public interface InboundGateway {
    @Gateway(requestChannel = "requestSyncChannel", replyChannel = "responseSyncChannel")
    Message syncRequest(CommonModel model, @Headers Map<String, Object> headers);

    @Gateway(requestChannel = "requestAsyncChannel")
    void aSyncRequest(CommonModel model, @Headers Map<String, Object> headers);

    @Gateway(requestChannel = "internalRequestChannel")
    void internalRequest(CommonModel model);

    @Gateway(requestChannel = "preparationQueueChannel")
    void preparation(Message<CommonModel> modelMessage);

    @Gateway(requestChannel = "senderQueueChannel")
    void sender(Message<CommonModel> modelMessage);

    @Gateway(requestChannel = "asyncQueueChannel")
    void async(Message<CommonModel> modelMessage);

    @Gateway(requestChannel = "monitoringQueueChannel")
    void monitoring(Message<MonitoringModel> model);

}
