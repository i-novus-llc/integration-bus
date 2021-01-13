package ru.i_novus.integration.registry.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import ru.i_novus.integration.registry.api.service.AuthorizationService;
import ru.i_novus.integration.registry.backend.client.AuthGatewayClient;
import ru.i_novus.integration.registry.backend.client.KeycloakClient;

@Configuration
public class RegistryConfiguration {

    @Bean
    @Conditional(AuthGatewayCondition.class)
    public AuthorizationService getAuthGateway(RegistryProperties properties) {
        return new AuthGatewayClient(properties);
    }

    @Bean
    @Conditional(KeycloakCondition.class)
    public AuthorizationService getKeycloakClient(RegistryProperties properties) {
        return new KeycloakClient(properties);
    }
}
