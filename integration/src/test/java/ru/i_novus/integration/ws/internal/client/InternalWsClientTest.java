package ru.i_novus.integration.ws.internal.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.integration.support.MessageBuilder;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.configuration.IntegrationProperties;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.service.MonitoringService;
import ru.i_novus.integration.ws.internal.api.DocumentData;
import ru.i_novus.integration.ws.internal.api.IntegrationMessage;
import ru.i_novus.integration.ws.internal.api.MessageData;
import ru.i_novus.integration.ws.internal.api.SplitDocumentModel;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InternalWsClientTest {

    @Mock
    private MonitoringService monitoringService;

    @Mock
    @SuppressWarnings("unused")
    private MonitoringGateway monitoringGateway;

    @Mock
    @SuppressWarnings("unused")
    private IntegrationProperties properties;

    @InjectMocks
    private InternalWsClient client;

    @Test
    public void sendInternal() {
        client.sendInternal(MessageBuilder.withPayload(new CommonModel()).build());
        verify(monitoringService, Mockito.never()).createError(any());

        CommonModel payload = new CommonModel();
        IntegrationMessage integrationMessage = new IntegrationMessage();
        MessageData messageData = new MessageData();
        DocumentData documentData = new DocumentData();
        SplitDocumentModel splitDocumentModel = new SplitDocumentModel();
        splitDocumentModel.setTemporaryPath("temp");
        documentData.setSplitDocument(splitDocumentModel);
        messageData.setAppData(documentData);
        integrationMessage.setMessage(messageData);
        payload.setObject(integrationMessage);
        payload.setMonitoringModel(new MonitoringModel());
        try {
            client.sendInternal(MessageBuilder.withPayload(payload).build());
        } catch (Exception e) {
            assertNotNull(e.getCause());
            assertEquals("No file parts for temp", e.getCause().getMessage());
        }
    }
}