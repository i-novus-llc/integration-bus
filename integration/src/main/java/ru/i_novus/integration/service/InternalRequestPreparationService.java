package ru.i_novus.integration.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
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
import java.util.UUID;

@Component
public class InternalRequestPreparationService {
    private static final Logger logger = LoggerFactory.getLogger(InternalWsClient.class);

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
            messageData.setGroupUid(UUID.randomUUID().toString());
            messageData.setUuid(UUID.randomUUID().toString());

            InternalRequestModel internalRequestModel = (InternalRequestModel) modelMessage.getPayload().getObject();

            DataModel dataModel = internalRequestModel.getDataModel();

            DocumentData documentData = new DocumentData();
            documentData.setDocFormat(dataModel.getMime());
            documentData.setDocName(dataModel.getName());
            documentData.setSplitDocument(storageService.prepareSplitModel(dataModel.getPath(),
                    modelMessage.getPayload().getMonitoringModel().getUid()));
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
            logger.error("Error on preparePackage", e);
            modelMessage.getPayload().getMonitoringModel().setError(e.getMessage() + " StackTrace: " + ExceptionUtils.getStackTrace(e));
            monitoringGateway.createError(MessageBuilder.withPayload(modelMessage.getPayload().getMonitoringModel()).build());
        }

        return MessageBuilder.withPayload(integrationRequest).build();

    }

}
