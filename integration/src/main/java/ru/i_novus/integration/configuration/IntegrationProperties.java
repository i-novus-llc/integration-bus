package ru.i_novus.integration.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource(value = {"application.properties","file:${app.home}/placeholders.properties"}, ignoreResourceNotFound=true)
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
    String internalWsTimeOut;


    public String getTempPath() {
        return tempPath;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public String getMonitoringAddress() {
        return monitoringAddress;
    }

    public String getAmqBrokerUrl() {
        return amqBrokerUrl;
    }

    public String getEnvCode() {
        return envCode;
    }

    public String getAdapterUrl() {
        return adapterUrl;
    }

    public String getInternalWsTimeOut() {
        return internalWsTimeOut;
    }
}
