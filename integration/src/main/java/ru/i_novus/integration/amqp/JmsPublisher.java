package ru.i_novus.integration.amqp;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.common.api.MonitoringModel;
import ru.i_novus.integration.model.CommonModel;

@Component
public class JmsPublisher {
    private final JmsTemplate jmsTemplate;

    public JmsPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void preparation(Message<CommonModel> apple){
        jmsTemplate.convertAndSend("preparation.queue", apple);
    }

    public void sender(Message<CommonModel> apple){
        jmsTemplate.convertAndSend("sender.queue", apple);
    }

    public void async(Message<CommonModel> apple){
        jmsTemplate.convertAndSend("async.queue", apple);
    }

    public void monitoring(Message<MonitoringModel> apple){
        jmsTemplate.convertAndSend("monitoring.queue", apple);
    }
}
