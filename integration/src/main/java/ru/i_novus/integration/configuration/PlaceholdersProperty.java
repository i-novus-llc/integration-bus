package ru.i_novus.integration.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@Getter
@PropertySource(value = {"application.properties","file:${app.home}/placeholders.properties"}, ignoreResourceNotFound=true)
public class PlaceholdersProperty {

    @Value("${file.storage.temp.path:/home/}")
    String tempPath;
    @Value("${registry.address:http://localhost:8090/registry}")
    String registryAddress;
    @Value("${monitoring.address:http://localhost:8099/monitoring}")
    String monitoringAddress;
    @Value("${amq.broker.url:vm://embedded?broker.persistent=false,useShutdownHook=false}")
    String amqBrokerUrl;
    @Value("${env.code:default}")
    String envCode;
    @Value("${central.adapter.url:http://localhost:8080/ws/internal}")
    String adapterUrl;
    @Value("${internal.ws.timeout:300000}")
    String internalWsTimeOut;

}
