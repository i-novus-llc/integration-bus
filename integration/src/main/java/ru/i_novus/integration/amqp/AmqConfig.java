package ru.i_novus.integration.amqp;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration
public class AmqConfig {
    private static final String SENDER_QUEUE = "sender.queue";
    private static final String RECEIVER_QUEUE = "receiver.queue";
    private static final String MONITORING_QUEUE = "monitoring.queue";

    @Value("${broker-url}")
    private String jmsBrokerUrl;

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setInitialRedeliveryDelay(10000);
        redeliveryPolicy.setUseCollisionAvoidance(true);
        redeliveryPolicy.setRedeliveryDelay(10000);
        redeliveryPolicy.setUseExponentialBackOff(false);
        redeliveryPolicy.setMaximumRedeliveries(-1);

        return redeliveryPolicy;
    }

    @Bean
    public ActiveMQConnectionFactory jmsConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(jmsBrokerUrl);
        connectionFactory.setNonBlockingRedelivery(true);
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy());
        connectionFactory.setTrustAllPackages(true);

        return connectionFactory;
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(jmsConnectionFactory());
        cachingConnectionFactory.setCacheProducers(false);
        cachingConnectionFactory.setSessionCacheSize(10);

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
