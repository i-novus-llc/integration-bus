package ru.i_novus.integration.registry.backend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.i_novus.integration.registry.api.service.AuthorizationService;
import ru.i_novus.integration.registry.backend.config.RegistryProperties;

import java.util.Map;

@Component
public class AuthGatewayClient implements AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthGatewayClient.class);
    private RegistryProperties properties;
    private volatile SignatureVerifier verifier;

    public AuthGatewayClient(RegistryProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean isValidToken(String token) {
        String[] array = token.split(" ");
        if (array.length < 2) {
            logger.error("Token is not valid: {}", token);
            throw new RuntimeException("Token is not valid");
        }
        JwtHelper.decodeAndVerify(array[1], getVerifier());
        return true;
    }

    private synchronized SignatureVerifier getVerifier() {
        RestTemplate restTemplate = new RestTemplate();
        if (verifier == null) {
            Map response = restTemplate.getForObject(properties.getAuthGatewayCertsUrl(), Map.class);
            if (response != null && response.get("value") != null)
                verifier = new RsaVerifier((String) response.get("value"));
        }
        return verifier;
    }


}
