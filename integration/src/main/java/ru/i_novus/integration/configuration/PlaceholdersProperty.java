package ru.i_novus.integration.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("file:${app.home}/placeholders.properties")
public class PlaceholdersProperty {

    @Value("${file.storage.temp.path}")
    String tempPath;
    @Value("${registry.address}")
    String registryAddress;
    @Value("${monitoring.address}")
    String monitoringAddress;
    @Value("${amq.broker.url}")
    String amqBrokerUrl;
    @Value("${env.code}")
    String envCode;
    @Value("${central.adapter.url}")
    String adapterUrl;
    @Value("${internal.ws.timeout:300000}")
    String internalWsTimeOut;

}
