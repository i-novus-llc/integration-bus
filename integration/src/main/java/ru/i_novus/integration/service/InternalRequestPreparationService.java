package ru.i_novus.integration.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ws.security.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.DataModel;
import ru.i_novus.integration.model.InputModel;
import ru.i_novus.integration.rest.client.RegistryClient;
import ru.i_novus.integration.ws.internal.client.InternalWsClient;
import ru.i_novus.integration.ws.internal.model.*;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
public class InternalRequestPreparationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalWsClient.class);

    @Autowired
    RegistryClient registryClient;
    @Autowired
    MonitoringGateway monitoringGateway;
    @Autowired
    FileStorageService storageService;

    public Message<CommonModel> preparePackage(Message<CommonModel> modelMessage) {
        CommonModel integrationRequest = new CommonModel();
        try {
            ObjectFactory objectFactory = new ObjectFactory();
            IntegrationMessage message = objectFactory.createIntegrationMessage();

            MessageData messageData = objectFactory.createMessageData();
            messageData.setGroupUid(UUIDGenerator.getUUID());
            messageData.setUuid(UUIDGenerator.getUUID());

            InputModel inputModel = (InputModel) modelMessage.getPayload().getObject();

            DataModel dataModel = inputModel.getDataModel();

            DocumentData documentData = objectFactory.createDocumentData();
            documentData.setDocFormat(dataModel.getMime());
            documentData.setDocName(dataModel.getName());
            documentData.setSplitDocument(storageService.prepareSplitModel(dataModel.getPath(), messageData.getGroupUid()));
            messageData.setAppData(documentData);

            MessageInfo messageInfo = objectFactory.createMessageInfo();
            messageInfo.setMessageId(modelMessage.getPayload().getMonitoringModel().getUid());
            messageInfo.setRecipient(inputModel.getRecipient());
            messageInfo.setSender(modelMessage.getPayload().getMonitoringModel().getSender());
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date());
            XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            messageInfo.setDate(date2);

            message.setMessage(messageData);
            message.setMessageData(messageInfo);

            integrationRequest.setObject(message);
            integrationRequest.setMonitoringModel(modelMessage.getPayload().getMonitoringModel());
            integrationRequest.setParticipantModel(modelMessage.getPayload().getParticipantModel());

            Files.deleteIfExists(Paths.get(dataModel.getPath()));
        } catch (Exception e) {
            LOGGER.info(ExceptionUtils.getStackTrace(e));
            modelMessage.getPayload().getMonitoringModel().setError(e.getMessage() + " StackTrace: " + ExceptionUtils.getStackTrace(e));
            monitoringGateway.createError(MessageBuilder.withPayload(modelMessage.getPayload().getMonitoringModel()).build());
        }

        return MessageBuilder.createMessage(integrationRequest,
                new MessageHeaders(Collections.singletonMap("url", modelMessage.getPayload().getParticipantModel().getUrl())));

    }

}
