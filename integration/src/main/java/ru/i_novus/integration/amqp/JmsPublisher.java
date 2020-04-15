package ru.i_novus.integration.amqp;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class JmsPublisher {
    private final JmsTemplate jmsTemplate;

    public JmsPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void preparation(Message apple){
        jmsTemplate.convertAndSend("preparation.queue", apple);
    }

    public void sender(Message apple){
        jmsTemplate.convertAndSend("sender.queue", apple);
    }

    public void async(Message apple){
        jmsTemplate.convertAndSend("async.queue", apple);
    }

    public void monitoring(Message apple){
        jmsTemplate.convertAndSend("monitoring.queue", apple);
    }
}
