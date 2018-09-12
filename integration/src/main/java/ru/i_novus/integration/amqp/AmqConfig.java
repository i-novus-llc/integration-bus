package ru.i_novus.integration.amqp;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import ru.i_novus.integration.configuration.PlaceholdersProperty;

@Configuration
public class AmqConfig {
    private static final String SENDER_QUEUE = "sender.queue";
    private static final String RECEIVER_QUEUE = "receiver.queue";
    private static final String MONITORING_QUEUE = "monitoring.queue";

    @Autowired
    PlaceholdersProperty property;

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setUseCollisionAvoidance(false);
        redeliveryPolicy.setRedeliveryDelay(1800000);
        redeliveryPolicy.setUseExponentialBackOff(false);
        redeliveryPolicy.setMaximumRedeliveries(-1);

        return redeliveryPolicy;
    }

    @Bean
    public ActiveMQConnectionFactory jmsConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(property.getAmqBrokerUrl());
        connectionFactory.setNonBlockingRedelivery(true);
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy());
        connectionFactory.setTrustAllPackages(true);

        return connectionFactory;
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(jmsConnectionFactory());
        cachingConnectionFactory.setReconnectOnException(true);

        return cachingConnectionFactory;
    }

    @Bean
    public ActiveMQQueue senderQueue() {
        return new ActiveMQQueue(SENDER_QUEUE);
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
