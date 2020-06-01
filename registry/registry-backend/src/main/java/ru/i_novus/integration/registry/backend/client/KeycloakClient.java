package ru.i_novus.integration.registry.backend.client;

import lombok.AllArgsConstructor;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.registry.backend.config.RegistryProperties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@AllArgsConstructor
public class KeycloakClient {

    private final RegistryProperties property;

    public Response checkToken(String token) {
        Response response = WebClient
                .create(property.getKeycloakUserInfoUrl())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .get();
        return Response.status(response.getStatus()).entity(response.readEntity(String.class)).build();
    }
}
