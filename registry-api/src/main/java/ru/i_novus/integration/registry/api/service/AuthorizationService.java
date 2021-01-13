package ru.i_novus.integration.registry.api.service;

public interface AuthorizationService {
    boolean isValidToken(String token);
}
