package ru.i_novus.integration.ws.internal.client;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.Fault;
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
import java.util.concurrent.*;

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

        logger.info("Try to getPort {}", recipientUrl);
        Client port = getPort(recipientUrl, property.getInternalWsTimeOut() / 3 + 1);

        logger.info("Try to invoke {}", recipientUrl);
        try {
            Object[] invoke = port.invoke(method, integrationMessage, recipientUrl, method);
            logger.info("Invocation completed");
            return invoke;
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
            logger.info("Try to send {} parts for {}", files.length, splitModel.getTemporaryPath());

            //поочередная отправка файлов потребителю
            for (int index = 1; index <= files.length; index++) {
                logger.info("Try to send part {}: {}", index - 1, files[index - 1].getPath());
                splitModel.setBinaryData(IntegrationFileUtils.prepareDataHandler(files[index - 1].getPath()));
                splitModel.setCount(index);
                message.getMessage().getAppData().setSplitDocument(splitModel);
                if (index == files.length) {
                    splitModel.setIsLast(true);
                }
                List result = null;
                boolean success = false;
                int retriesCount = 0;
                do {
                    retriesCount++;
                    try {
                        Client wsClient = getPort(property.getAdapterUrl(), property.getInternalWsTimeOut());
                        result = (List) wsClient.invoke("adapter", jaxbToString(message), request.getHeaders().get("url", String.class),
                                request.getHeaders().get("method", String.class))[0];
                        success = result != null && !result.isEmpty() && result.get(0) instanceof Boolean && (Boolean) result.get(0);
                    } catch (Fault e) {
                        if (retriesCount >= 10) {
                            throw new RuntimeException(
                                    String.format("Sending part %s retries exceeded, remains %s",
                                    files[index - 1].getPath(), files.length - index), e);
                        }
                    }
                    logger.info("Sent part {}: {}. Result: {}", index - 1, files[index - 1].getPath(), result);
                } while (!success);

                Files.deleteIfExists(Paths.get(files[index - 1].getPath()));
            }

            FileUtils.deleteDirectory(new File(splitModel.getTemporaryPath()));
            monitoringService.fineStatus(request.getPayload());
        } catch (Exception e) {
            logger.error("Error on sending part of {}, to adapter {}, receiver {}",
                    splitModel.getTemporaryPath(), property.getAdapterUrl(),
                    request.getPayload().getParticipantModel() == null ?
                            "<empty>" : request.getPayload().getParticipantModel().getReceiver(),
                    e);
            request.getPayload().getMonitoringModel().setError(e.getMessage() + " StackTrace: " + ExceptionUtils.getStackTrace(e));
            monitoringGateway.createError(MessageBuilder.withPayload(request.getPayload().getMonitoringModel()).build());
            throw new RuntimeException(e);
        }
    }

    /**
     * Подготовка клиента по wsdl url
     */
    private Client getPort(String serviceUrl, Long timeout) {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client;
        String wsdlUrl = serviceUrl + "?wsdl";

        ExecutorService executorService = new ThreadPoolExecutor(0, 10,
                timeout, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());

        try {
            Future<Client> future = executorService.submit(() -> dcf.createClient(wsdlUrl));
            client = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("Can not create port by wsdl {}", wsdlUrl);
            throw new RuntimeException(e);
        }

        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setAutoRedirect(true);
        policy.setReceiveTimeout(timeout);
        policy.setConnectionTimeout(timeout);
        ((HTTPConduit) client.getConduit()).setClient(policy);

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


