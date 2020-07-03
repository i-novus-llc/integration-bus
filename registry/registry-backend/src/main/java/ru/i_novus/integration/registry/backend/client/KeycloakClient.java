package ru.i_novus.integration.registry.backend.client;

import lombok.AllArgsConstructor;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.i_novus.integration.registry.backend.api.AuthorizationService;
import ru.i_novus.integration.registry.backend.config.RegistryProperties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@AllArgsConstructor
public class KeycloakClient implements AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(KeycloakClient.class);
    private final RegistryProperties property;

    @Override
    public boolean isValidToken(String token) {
        Response response = WebClient
                .create(property.getKeycloakUserInfoUrl())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .get();
        boolean result = response.getStatus() == Response.Status.OK.getStatusCode();
        if (!result) {
            logger.warn("Token {}... is not valid: status - {}, body - {}", token.substring(0,
                    Math.min(token.length(), 20)), response.getStatus(), response.readEntity(String.class));
        }
        return result;
    }
}
