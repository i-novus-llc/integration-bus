package ru.i_novus.integration.amqp;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
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
    public ActiveMQConnectionFactory jmsConnectionFactory(RedeliveryPolicy redeliveryPolicy) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(properties.getAmqBrokerUrl());
        connectionFactory.setNonBlockingRedelivery(true);
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        connectionFactory.setTrustAllPackages(true);
        connectionFactory.setPrefetchPolicy(activeMQPrefetchPolicy());

        return connectionFactory;
    }

    @Bean
    public ActiveMQPrefetchPolicy activeMQPrefetchPolicy() {
        ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
        prefetchPolicy.setQueuePrefetch(1);

        return prefetchPolicy;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setSessionTransacted(true);

        return jmsTemplate;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                      DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionTransacted(true);

        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsSenderListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionTransacted(true);
        factory.setConcurrency(properties.getQueueSenderConcurrent());

        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsPreparationListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                                 DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionTransacted(true);
        factory.setConcurrency(properties.getQueuePreparationConcurrent());

        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsAsyncListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                           DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionTransacted(true);
        factory.setConcurrency(properties.getQueueAsyncConcurrent());

        configurer.configure(factory, connectionFactory);
        return factory;
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
