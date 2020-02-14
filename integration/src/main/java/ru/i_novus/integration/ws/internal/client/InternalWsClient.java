package ru.i_novus.integration.ws.internal.client;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@SuppressWarnings("WeakerAccess")
public class InternalWsClient {
    private static final Logger logger = LoggerFactory.getLogger(InternalWsClient.class);

    private final IntegrationProperties property;
    private final MonitoringService monitoringService;
    private final MonitoringGateway monitoringGateway;

    public InternalWsClient(IntegrationProperties property, MonitoringService monitoringService, MonitoringGateway monitoringGateway) {
        this.property = property;
        this.monitoringService = monitoringService;
        this.monitoringGateway = monitoringGateway;
    }

    /**
     * Отправка сообщения потребителю
     */
    public Object[] sendRequest(String integrationMessage, String recipientUrl, String method) {
        try {
            return getPort(recipientUrl).invoke(method, integrationMessage, recipientUrl, method);
        } catch (Exception e) {
            logger.error("Failed sendRequest to recipient {}, method {}, integrationMessage {}",
                    recipientUrl, method, integrationMessage, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Подготовка и отправка файла потребителю
     */
    public void sendInternal(Message<CommonModel> request) {
        if (request.getPayload().getObject() == null) {
            logger.error("Empty object in message: {}", request.getPayload());
            return;
        }

        IntegrationMessage message = (IntegrationMessage) request.getPayload().getObject();
        SplitDocumentModel splitModel = message.getMessage().getAppData().getSplitDocument();

        try {
            File[] files = new File(splitModel.getTemporaryPath()).listFiles();

            if (files == null || files.length == 0) {
                throw new RuntimeException("No file parts for " + splitModel.getTemporaryPath());
            }

            IntegrationFileUtils.sortedFilesByName(files);
            logger.debug("Try to send {} parts for {}", files.length, splitModel.getTemporaryPath());

            Client wsClient = getPort(property.getAdapterUrl());
            //поочередная отправка файлов потребителю
            for (int index = 1; index <= files.length; index++) {
                logger.debug("Try to send part {}: {}", index - 1, files[index - 1].getPath());
                splitModel.setBinaryData(IntegrationFileUtils.prepareDataHandler(files[index - 1].getPath()));
                splitModel.setCount(index);
                message.getMessage().getAppData().setSplitDocument(splitModel);
                if (index == files.length) {
                    splitModel.setIsLast(true);
                }
                List result = (List) wsClient.invoke("adapter", jaxbToString(message), request.getHeaders().get("url", String.class),
                        request.getHeaders().get("method", String.class))[0];
                logger.debug("Sent part {}: {}. Result: {}", index - 1, files[index - 1].getPath(), result);
                if (result != null && !result.isEmpty() && result.get(0) instanceof Boolean && (Boolean) result.get(0)) {
                    Files.deleteIfExists(Paths.get(files[index - 1].getPath()));
                } else {
                    throw new RuntimeException(result != null ? result.toString() : "result = null");
                }
            }

            FileUtils.deleteDirectory(new File(splitModel.getTemporaryPath()));
            monitoringService.fineStatus(request.getPayload());
        } catch (Exception e) {
            logger.error("Error on sending part of {}", splitModel.getTemporaryPath(), e);
            request.getPayload().getMonitoringModel().setError(e.getMessage() + " StackTrace: " + ExceptionUtils.getStackTrace(e));
            monitoringGateway.createError(MessageBuilder.withPayload(request.getPayload().getMonitoringModel()).build());
            throw new RuntimeException(e);
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
        policy.setReceiveTimeout(Long.parseLong(property.getInternalWsTimeOut()));
        policy.setConnectionTimeout(Long.parseLong(property.getInternalWsTimeOut()));
        conduit.setClient(policy);

        return client;
    }

    private String jaxbToString(IntegrationMessage message) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(IntegrationMessage.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(message, sw);

        return sw.toString();
    }
}


