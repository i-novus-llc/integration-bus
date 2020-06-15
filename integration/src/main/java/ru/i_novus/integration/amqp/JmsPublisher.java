package ru.i_novus.integration.amqp;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.model.CommonModel;

@Component
public class JmsPublisher {
    private final JmsTemplate jmsTemplate;

    public JmsPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void preparation(Message<CommonModel> apple) {

        jmsTemplate.convertAndSend("preparation.queue", apple, (message) -> {
            message.setJMSCorrelationID(apple.getPayload().getMonitoringModel().getSender() + " -> "
                    + apple.getPayload().getMonitoringModel().getReceiver() + " uid: " + apple.getPayload().getMonitoringModel().getUid());
            return message;
        });
    }

    public void sender(Message<CommonModel> apple) {
        jmsTemplate.convertAndSend("sender.queue", apple, (message) -> {
            message.setJMSCorrelationID(apple.getPayload().getMonitoringModel().getSender() + " -> "
                    + apple.getPayload().getMonitoringModel().getReceiver() + " uid: " + apple.getPayload().getMonitoringModel().getUid());
            return message;
        });
    }

    public void async(Message<CommonModel> apple) {
        jmsTemplate.convertAndSend("async.queue", apple, (message) -> {
            message.setStringProperty("Authorization", (String) apple.getHeaders().get("Authorization"));
            return message;
        });
    }

    public void monitoring(Message<MonitoringModel> apple) {
        jmsTemplate.convertAndSend("monitoring.queue", apple);
    }
}
