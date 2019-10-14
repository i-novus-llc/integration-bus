package ru.i_novus.integration.registry.backend.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.i_novus.ms.audit.client.AuditClient;
import ru.i_novus.ms.audit.client.model.AuditClientRequest;
import net.n2oapp.security.auth.common.User;

@Component
public class RegistryAuditClient {
    @Autowired
    private AuditClient auditClient;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

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

        User user = getUser();
        if (user != null) {
            request.setUserId(user.getEmail());
            request.setUsername(user.getUsername());
        }

        auditClient.add(request);
    }

    private User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            if (auth.getPrincipal() instanceof User) {
                return (User) auth.getPrincipal();
            } else {
                return new User("" + auth.getPrincipal());
            }
        }
        return null;
    }

}
