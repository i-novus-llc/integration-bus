package ru.i_novus.integration.ws.internal.client;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.IntegrationProperties;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.service.IntegrationFileUtils;
import ru.i_novus.integration.service.MonitoringService;
import ru.i_novus.integration.ws.internal.api.IntegrationMessage;
import ru.i_novus.integration.ws.internal.api.SplitDocumentModel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPBinding;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * WsClient.
 *
 * @author asamoilov
 */
@Component
public class InternalWsClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalWsClient.class);

    private final IntegrationProperties property;
    private final MonitoringService monitoringService;
    private final MonitoringGateway monitoringGateway;

    @Autowired
    public InternalWsClient(IntegrationProperties property, MonitoringService monitoringService, MonitoringGateway monitoringGateway) {
        this.property = property;
        this.monitoringService = monitoringService;
        this.monitoringGateway = monitoringGateway;
    }

    /**
     * Отправка сообщения потребителю
     */
    public Object[] sendRequest(String integrationMessage, String recipientUrl, String method) throws Exception {
        return getPort(recipientUrl).invoke(method, integrationMessage, recipientUrl, method);
    }

    /**
     * Отправка сообщения на адаптер для переадрисации потребителю
     */
    private Object[] sendAdapter(String integrationMessage, String recipientUrl, String method) throws Exception {
        return getPort(property.getAdapterUrl()).invoke("adapter", integrationMessage, recipientUrl, method);
    }

    /**
     * Подготовка и отправка файла потребителю
     */
    public void sendInternal(Message<CommonModel> request) {
        LOGGER.info("start send");
        if (request.getPayload().getObject() != null) {
            try {
                LOGGER.info("1");
                IntegrationMessage message = (IntegrationMessage) request.getPayload().getObject();
                SplitDocumentModel splitModel = message.getMessage().getAppData().getSplitDocument();
                File[] files = new File(splitModel.getTemporaryPath()).listFiles();
                IntegrationFileUtils.sortedFilesByName(files);

                LOGGER.info("2");
                if (files != null) {
                    LOGGER.info("3");
                    Client wsClient = getPort(property.getAdapterUrl());
                    //поочередная отправка файлов потребителю
                    LOGGER.info("4");
                    for (int index = 1; index <= files.length; index++) {
                        splitModel.setBinaryData(IntegrationFileUtils.prepareDataHandler(files[index - 1].getPath()));
                        splitModel.setCount(index);
                        message.getMessage().getAppData().setSplitDocument(splitModel);
                        if (index == files.length) {
                            splitModel.setIsLast(true);
                        }
                        LOGGER.info("5");
                        List result = (List) wsClient.invoke("adapter", jaxbToString(message), request.getHeaders().get("url", String.class),
                                request.getHeaders().get("method", String.class))[0];
                        LOGGER.info("6");
                        if (result != null && !result.isEmpty() && result.get(0) instanceof Boolean && (Boolean) result.get(0)) {
                            Files.deleteIfExists(Paths.get(files[index - 1].getPath()));
                        } else {
                            throw new RuntimeException(result != null ? String.join(", ", result) : "result = null");
                        }
                    }
                }

                FileUtils.deleteDirectory(new File(splitModel.getTemporaryPath()));
                monitoringService.fineStatus(request.getPayload());
            } catch (Exception e) {
                request.getPayload().getMonitoringModel().setError(e.getMessage() + " StackTrace: " + ExceptionUtils.getStackTrace(e));
                monitoringGateway.createError(MessageBuilder.withPayload(request.getPayload().getMonitoringModel()).build());
                LOGGER.info(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Подготовка клиента по wsdl url
     */
    private Client getPort(String wsdlUrl) {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(wsdlUrl + "?wsdl");

        HTTPConduit conduit = (HTTPConduit) client.getConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setAutoRedirect(true);
        policy.setReceiveTimeout(Long.valueOf(property.getInternalWsTimeOut()));
        policy.setConnectionTimeout(Long.valueOf(property.getInternalWsTimeOut()));
        conduit.setClient(policy);

        return client;
    }

    private void bindingSOAPHandler(Object port, SOAPHandler<SOAPMessageContext> soapHandler) {
        BindingProvider provider = (BindingProvider) port;
        Binding binding = provider.getBinding();
        ((SOAPBinding) binding).setMTOMEnabled(true);
        List<Handler> handlerChain = binding.getHandlerChain();
        handlerChain.add(soapHandler);
        binding.setHandlerChain(handlerChain);
    }

    private String jaxbToString(IntegrationMessage message) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(IntegrationMessage.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(message, sw);

        return sw.toString();
    }
}


