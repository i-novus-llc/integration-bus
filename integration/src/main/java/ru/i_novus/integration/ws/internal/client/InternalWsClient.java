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
import ru.i_novus.integration.common.api.model.ParticipantModel;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            File sendDir = new File(splitModel.getTemporaryPath() + "_SEND");
            sendDir.mkdirs();

            if (files == null || files.length == 0) {
                if (sendDir.length() != splitModel.getCount()) {
                    request.getPayload().getMonitoringModel().setError(" count of files does not match the original count, send aborted!! ");
                    monitoringGateway.createError(MessageBuilder.withPayload(request.getPayload().getMonitoringModel()).build());
                    deleteTempDirs(new File(splitModel.getTemporaryPath()), sendDir);
                    return;
                } else {
                    request.getPayload().getMonitoringModel().setError(" split files dir is empty, send aborted!! ");
                    monitoringGateway.createError(MessageBuilder.withPayload(request.getPayload().getMonitoringModel()).build());
                }
                return;
            }

            IntegrationFileUtils.sortedFilesByName(files);
            logger.info("Try to send {} parts for {}", files.length, splitModel.getTemporaryPath());

            //поочередная отправка файлов потребителю
            for (int index = 1; index <= files.length; index++) {
                logger.info("Try to send part {}: {}", index - 1, files[index - 1].getPath());
                splitModel.setBinaryData(IntegrationFileUtils.prepareDataHandler(files[index - 1].getPath()));

                message.getMessage().getAppData().setSplitDocument(splitModel);
                if (index == files.length) {
                    splitModel.setIsLast(true);
                }
                Object[] result = null;
                boolean success = false;
                int retriesCount = 0;
                do {
                    retriesCount++;
                    try {
                        ParticipantModel participantModel = request.getPayload().getParticipantModel();
                        Client wsClient = getPort(property.getAdapterUrl(), property.getInternalWsTimeOut());
                        result = wsClient.invoke(participantModel.getMethod(), jaxbToString(message));

                        success = result != null && result.length != 0 && result[0] instanceof Boolean && (Boolean) result[0];
                    } catch (Fault e) {
                        logger.error("Failed try number {} to send part {}: {}, error: {}",
                                retriesCount, index - 1, files[index - 1].getPath(), e.getMessage());
                        if (retriesCount >= 10) {
                            throw new RuntimeException(
                                    String.format("Sending part %s retries exceeded, remains %s parts",
                                            files[index - 1].getPath(), files.length - index), e);
                        }
                    }
                } while (!success);
                logger.info("Successfully sent part {}: {}. Result: {}", index - 1, files[index - 1].getPath(), result);

                Files.copy(Paths.get(sendDir.getPath()), new FileOutputStream(files[index - 1].getPath()));
                Files.deleteIfExists(Paths.get(files[index - 1].getPath()));
            }

            deleteTempDirs(new File(splitModel.getTemporaryPath()), sendDir);
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

    private void deleteTempDirs(File prepareDir, File sendDir) throws IOException {
        FileUtils.deleteDirectory(prepareDir);
        FileUtils.deleteDirectory(sendDir);
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
