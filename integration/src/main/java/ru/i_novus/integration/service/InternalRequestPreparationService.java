package ru.i_novus.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import ru.i_novus.integration.model.InputModel;
import ru.i_novus.integration.rest.RegistryClient;
import ru.i_novus.integration.ws.internal.model.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

public class InternalRequestPreparationService {

    @Autowired
    RegistryClient registryClient;

    public Message<IntegrationMessage> requestASync(InputModel msg) throws IOException, DatatypeConfigurationException {
        ObjectFactory objectFactory = new ObjectFactory();
        IntegrationMessage message = objectFactory.createIntegrationMessage();

        MessageData messageData = objectFactory.createMessageData();
        messageData.setGroupUid("test");
        messageData.setUuid("test");

        DocumentData documentData = objectFactory.createDocumentData();
        documentData.setBinaryData(getDocumentByStorage(msg.getPath()));
        documentData.setDigestData("test");
        documentData.setDocFormat("test");
        documentData.setDocName("test");
        documentData.setDocType(1);
        messageData.getAppData().add(documentData);

        MessageInfo messageInfo = objectFactory.createMessageInfo();
        messageInfo.setMessageId("test");
        messageInfo.setRecipient("test");
        messageInfo.setSender("test");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        messageInfo.setDate(date2);

        message.setMessage(messageData);
        message.setMessageData(messageInfo);

        return MessageBuilder.createMessage(message,
                new MessageHeaders(Collections.singletonMap("url", registryClient.getServiceUrlByCode(msg.getRecipient()))));

    }

    private DataHandler getDocumentByStorage(String filePath) throws IOException {

        try (InputStream docStream = new FileInputStream(new File(filePath))) {
            byte[] fromFile;
            try {
                fromFile = new byte[docStream.available()];
                docStream.read(fromFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new DataHandler( new FileDataSource(new File(filePath)));
        }
    }
}
