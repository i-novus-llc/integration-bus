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

    @JmsListener(destination = "preparation.queue")
    public void preparation(final CommonModel task){
        gateway.preparation(task);
    }

    @JmsListener(destination = "sender.queue")
    public void sender(final CommonModel task){
        gateway.sender(task);
    }

    @JmsListener(destination = "async.queue")
    public void async(final CommonModel task){
        gateway.async(task);
    }

    @JmsListener(destination = "monitoring.queue")
    public void monitoring(final MonitoringModel task){
        gateway.monitoring(task);
    }
}
