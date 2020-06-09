package ru.i_novus.integration.registry.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource(value = "application.properties", ignoreResourceNotFound = true)
public class RegistryProperties {
    @Value("${keycloak.user-info-url}")
    private String keycloakUserInfoUrl;
}
