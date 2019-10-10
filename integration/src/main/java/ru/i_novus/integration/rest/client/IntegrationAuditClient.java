package ru.i_novus.integration.rest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.i_novus.is.integration.common.api.MonitoringModel;
import ru.i_novus.ms.audit.client.AuditClient;
import ru.i_novus.ms.audit.client.model.AuditClientRequest;

@Component
public class IntegrationAuditClient {
    @Autowired
    private AuditClient auditClient;
    @Autowired
    private ObjectMapper mapper;

    public void sendMonitoringMessage(MonitoringModel model) throws JsonProcessingException {
        AuditClientRequest request = new AuditClientRequest();
        request.setObjectType(model.getClass().getSimpleName());
        request.setObjectId(model.getUid());
        request.setAuditType((short) 2);
        request.setSourceApplication("Integration");
        request.setSender(model.getSender());
        request.setReceiver(model.getReceiver());
        request.setEventType(model.getError() == null ? model.getOperation() : "error");
        request.setContext(mapper.writeValueAsString(model));
        auditClient.add(request);
    }
}
