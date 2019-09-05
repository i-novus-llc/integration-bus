package ru.i_novus.integration.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource(value = {"application.properties","file:${app.home}/placeholders.properties"}, ignoreResourceNotFound=true)
public class PlaceholdersProperty {

    @Value("${integration.fileStoragePath}")
    String tempPath;
    @Value("${integration.registry.url}")
    String registryAddress;
    @Value("${integration.monitoring.url}")
    String monitoringAddress;
    @Value("${integration.amqBroker.url}")
    String amqBrokerUrl;
    @Value("${integration.envCode}")
    String envCode;
    @Value("${integration.centralAdapter.url}")
    String adapterUrl;
    @Value("${integration.internalWsTimeout}")
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
