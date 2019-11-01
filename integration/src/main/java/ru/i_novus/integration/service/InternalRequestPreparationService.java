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
import ru.i_novus.integration.model.InternalRequestModel;
import ru.i_novus.integration.ws.internal.api.DocumentData;
import ru.i_novus.integration.ws.internal.api.IntegrationMessage;
import ru.i_novus.integration.ws.internal.api.MessageData;
import ru.i_novus.integration.ws.internal.api.MessageInfo;
import ru.i_novus.integration.ws.internal.client.InternalWsClient;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class InternalRequestPreparationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalWsClient.class);

    private final MonitoringGateway monitoringGateway;
    private final FileService storageService;

    @Autowired
    public InternalRequestPreparationService(MonitoringGateway monitoringGateway, FileService storageService) {
        this.monitoringGateway = monitoringGateway;
        this.storageService = storageService;
    }

    public Message<CommonModel> preparePackage(Message<CommonModel> modelMessage) {
        CommonModel integrationRequest = new CommonModel();
        try {
            IntegrationMessage message = new IntegrationMessage();

            MessageData messageData = new MessageData();
            messageData.setGroupUid(UUIDGenerator.getUUID());
            messageData.setUuid(UUIDGenerator.getUUID());

            InternalRequestModel internalRequestModel = (InternalRequestModel) modelMessage.getPayload().getObject();

            DataModel dataModel = internalRequestModel.getDataModel();

            DocumentData documentData = new DocumentData();
            documentData.setDocFormat(dataModel.getMime());
            documentData.setDocName(dataModel.getName());
            documentData.setSplitDocument(storageService.prepareSplitModel(dataModel.getPath(), messageData.getGroupUid()));
            messageData.setAppData(documentData);

            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessageId(modelMessage.getPayload().getMonitoringModel().getUid());
            messageInfo.setRecipient(internalRequestModel.getRecipient());
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
        Map<String, Object> messageHeaders = new HashMap<>();
        messageHeaders.put("url", modelMessage.getPayload().getParticipantModel().getUrl());
        messageHeaders.put("method", modelMessage.getPayload().getParticipantModel().getMethod());

        return MessageBuilder.createMessage(integrationRequest,
                new MessageHeaders(messageHeaders));

    }

}
