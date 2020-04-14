package ru.i_novus.integration.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import ru.i_novus.integration.configuration.IntegrationProperties;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class AmqConfig {
    private static final String SENDER_QUEUE = "sender.queue";
    private static final String RECEIVER_QUEUE = "receiver.queue";
    private static final String MONITORING_QUEUE = "monitoring.queue";
    private static final String PREPARATION_QUEUE = "preparation.queue";
    private static final String ASYNC_QUEUE = "async.queue";

    private final IntegrationProperties properties;

    @Autowired
    public AmqConfig(IntegrationProperties properties) {
        this.properties = properties;
    }


    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setUseCollisionAvoidance(true);
        redeliveryPolicy.setRedeliveryDelay(180000);
        redeliveryPolicy.setUseExponentialBackOff(false);
        redeliveryPolicy.setMaximumRedeliveries(-1);

        return redeliveryPolicy;
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(properties.getAmqBrokerUrl());
        connectionFactory.setNonBlockingRedelivery(true);
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy());
        connectionFactory.setTrustAllPackages(true);

        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(MessageConverter messageConverter, ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setMessageConverter(messageConverter);

        return jmsTemplate;
    }

    @Bean
    @Primary
    public JmsListenerContainerFactory<?> concurrentJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionTransacted(true);
        factory.setConcurrency(properties.getQueueConcurrent());
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }


    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper mapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(mapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public ActiveMQQueue senderQueue() {
        return new ActiveMQQueue(SENDER_QUEUE);
    }

    @Bean
    public ActiveMQQueue asyncQueue() {
        return new ActiveMQQueue(ASYNC_QUEUE);
    }

    @Bean
    public ActiveMQQueue preparationQueue() {
        return new ActiveMQQueue(PREPARATION_QUEUE);
    }

    @Bean
    public ActiveMQQueue receiverQueue() {
        return new ActiveMQQueue(RECEIVER_QUEUE);
    }

    @Bean
    public ActiveMQQueue monitoringQueue() {
        return new ActiveMQQueue(MONITORING_QUEUE);
    }
}
