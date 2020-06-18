package ru.i_novus.integration.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@Getter
@PropertySource(value = {"application.properties", "file:${app.home}/placeholders.properties"}, ignoreResourceNotFound = true)
public class IntegrationProperties {

    @Value("${integration.file-storage-path}")
    String tempPath;
    @Value("${integration.registry.url}")
    String registryAddress;
    @Value("${integration.monitoring.url}")
    String monitoringAddress;
    @Value("${integration.activemq.broker-url}")
    String amqBrokerUrl;

    @Value("${integration.env-code}")
    String envCode;
    @Value("${integration.central-adapter-url}")
    String adapterUrl;
    @Value("${integration.internal-ws-timeout}")
    Long internalWsTimeOut;
    @Value("${sender.queue.concurrent.consumers:50-50}")
    String queueSenderConcurrent;
    @Value("${preparation.queue.concurrent.consumers:5-10}")
    String queuePreparationConcurrent;
    @Value("${async.queue.concurrent.consumers:5-10}")
    String queueAsyncConcurrent;
    @Value("${integration.monitoring.client}")
    String monitoringClient;
}
