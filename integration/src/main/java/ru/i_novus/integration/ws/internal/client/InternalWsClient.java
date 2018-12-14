package ru.i_novus.integration.ws.internal.client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.jaxws.JaxWsClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.i_novus.common.sign.soap.SignatureSOAPHandler;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.service.FileStorageService;
import ru.i_novus.integration.service.MonitoringService;
import ru.i_novus.integration.ws.internal.model.IOException_Exception;
import ru.i_novus.integration.ws.internal.model.IntegrationMessage;
import ru.i_novus.integration.ws.internal.model.InternalWsEndpoint;
import ru.i_novus.integration.ws.internal.model.SplitDocumentModel;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPBinding;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InternalWsClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalWsClient.class);

    @Autowired
    PlaceholdersProperty property;
    @Autowired
    MonitoringService monitoringService;
    @Autowired
    MonitoringGateway monitoringGateway;
    @Autowired
    FileStorageService storageService;

    public Boolean sendRequest(Object request, String recipientUrl) {
        IntegrationMessage requestModel = (IntegrationMessage) request;

        InternalWsEndpoint integrationEndpoint = getPort();
        BindingProvider bindingProvider = (BindingProvider) integrationEndpoint;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, recipientUrl);
        try {
            return integrationEndpoint.request(requestModel);
        } catch (IOException_Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAdapter(Message<CommonModel<IntegrationMessage>> request) {
        if (request.getPayload().getObject() != null) {
            try {
                InternalWsEndpoint integrationEndpoint = getPort();
                BindingProvider bindingProvider = (BindingProvider) integrationEndpoint;
                bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, property.getAdapterUrl());

                IntegrationMessage message = request.getPayload().getObject();
                SplitDocumentModel splitModel = request.getPayload().getObject().getMessage().getAppData().getSplitDocument();
                List<File> list = Arrays.stream((new File(splitModel.getTemporaryPath()).listFiles())).collect(Collectors.toList());

                for (int index = 0; index <= list.size(); index++) {
                    splitModel.setBinaryData(storageService.prepareDataHandler(list.get(index).getPath()));
                    splitModel.setCount(index);
                    message.getMessage().getAppData().setSplitDocument(splitModel);
                    if (index == list.size()) {
                        splitModel.setIsLast(true);
                    }
                    if (integrationEndpoint.adapter(message, request.getHeaders().get("url", String.class))) {
                        Files.deleteIfExists(Paths.get(list.get(index).getPath()));
                    }
                }
                Files.deleteIfExists(Paths.get(splitModel.getTemporaryPath()));
                monitoringService.fineStatus(request.getPayload());
            } catch (Exception e) {
                request.getPayload().getMonitoringModel().setError(e.getMessage() + " StackTrace: " + ExceptionUtils.getStackTrace(e));
                monitoringGateway.createError(MessageBuilder.withPayload(request.getPayload().getMonitoringModel()).build());

                throw new RuntimeException(e);
            }
        }
    }

    private InternalWsEndpoint getPort() {
        InternalWsEndpoint port;
        Map<String, Object> props = new HashMap<>();
        props.put("mtom-enabled", Boolean.TRUE);
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(InternalWsEndpoint.class);
        factory.setProperties(props);
        port = ((InternalWsEndpoint) factory.create());

        HTTPConduit conduit = (HTTPConduit) JaxWsClientProxy.getClient(port).getConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setAutoRedirect(true);
        policy.setReceiveTimeout(Long.valueOf(property.getInternalWsTimeOut()));
        conduit.setClient(policy);

        KeyStore store = property.getKeyStore();

        bindingSOAPHandler(port,
                new SignatureSOAPHandler(store, property.getKeyStoreAlias(), property.getKeyStoreAliasPassword()));

        return port;
    }

    private void bindingSOAPHandler(Object port, SOAPHandler<SOAPMessageContext> soapHandler) {
        BindingProvider provider = (BindingProvider) port;
        Binding binding = provider.getBinding();
        ((SOAPBinding) binding).setMTOMEnabled(true);
        List<Handler> handlerChain = binding.getHandlerChain();
        handlerChain.add(soapHandler);
        binding.setHandlerChain(handlerChain);
    }
}


