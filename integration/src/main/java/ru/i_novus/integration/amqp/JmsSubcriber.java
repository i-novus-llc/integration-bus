package ru.i_novus.integration.amqp;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.i_novus.integration.common.api.MonitoringModel;
import ru.i_novus.integration.gateway.InboundGateway;
import ru.i_novus.integration.model.CommonModel;

@Component
@Transactional
public class JmsSubcriber {
    private final InboundGateway gateway;

    public JmsSubcriber(InboundGateway gateway) {
        this.gateway = gateway;
    }

    @JmsListener(destination = "preparation.queue", containerFactory = "concurrentJmsListenerContainerFactory")
    public void preparation(CommonModel msg){
        gateway.preparation(msg);
    }

    @JmsListener(destination = "sender.queue", containerFactory = "concurrentJmsListenerContainerFactory")
    public void sender(CommonModel msg){
        gateway.sender(msg);
    }

    @JmsListener(destination = "async.queue", containerFactory = "concurrentJmsListenerContainerFactory")
    public void async(CommonModel msg){
        gateway.async(msg);
    }

    @JmsListener(destination = "monitoring.queue", containerFactory = "concurrentJmsListenerContainerFactory")
    public void monitoring(MonitoringModel msg){
        gateway.monitoring(msg);
    }
}
