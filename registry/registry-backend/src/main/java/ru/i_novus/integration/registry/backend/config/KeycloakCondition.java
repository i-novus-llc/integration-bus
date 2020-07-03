package ru.i_novus.integration.registry.backend.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

public class KeycloakCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return Objects.requireNonNull(context.getEnvironment().getProperty("authorization.service")).equals(
                "keycloak");
    }
}
