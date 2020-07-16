package ru.i_novus.integration.registry.backend.api;

public interface AuthorizationService {
    boolean isValidToken(String token);
}
