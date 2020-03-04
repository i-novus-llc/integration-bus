package ru.i_novus.integration.registry.backend.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import ru.i_novus.ms.audit.client.AuditClient;
import ru.i_novus.ms.audit.client.model.AuditClientRequest;

@Component
public class RegistryAuditClient {
    private final AuditClient auditClient;
    private final ObjectMapper mapper;
    private final MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public RegistryAuditClient(AuditClient auditClient, ObjectMapper mapper, MessageSourceAccessor messageSourceAccessor) {
        this.auditClient = auditClient;
        this.mapper = mapper;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    public void audit(String action, Object object, String objectId, String objectName) {
        AuditClientRequest request = new AuditClientRequest();
        request.setEventType(messageSourceAccessor.getMessage(action));
        request.setObjectType(object.getClass().getSimpleName());
        request.setObjectId(objectId);
        try {
            request.setContext(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            request.setContext("" + object);
        }
        request.setObjectName(messageSourceAccessor.getMessage(objectName));
        request.setAuditType((short) 1);

        auditClient.add(request);
    }

}
