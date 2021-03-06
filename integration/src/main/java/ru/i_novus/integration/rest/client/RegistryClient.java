package ru.i_novus.integration.rest.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.common.api.model.ParticipantModel;
import ru.i_novus.integration.common.api.model.RegistryInfoModel;
import ru.i_novus.integration.configuration.IntegrationProperties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Locale;

@Component
public class RegistryClient {
    private static final Logger logger = LoggerFactory.getLogger(RegistryClient.class);
    private final IntegrationProperties property;
    private final MessageSource messageSource;
    private final JacksonJsonProvider provider;

    @Autowired
    public RegistryClient(IntegrationProperties property, MessageSource messageSource, JacksonJsonProvider provider) {
        this.property = property;
        this.messageSource = messageSource;
        this.provider = provider;
    }

    public ParticipantModel getServiceParticipant(String receiver, String sender, String method) throws IOException {
        RegistryInfoModel registryInfoModel = new RegistryInfoModel();
        registryInfoModel.setSender(sender);
        registryInfoModel.setReceiver(receiver);
        registryInfoModel.setMethod(method);
        WebClient client = getWebClient("/service/prepareRequest");

        try {
            Response response = client.post(registryInfoModel);
            checkResponseError(response);
            return response.readEntity(ParticipantModel.class);
        } finally {
            if (client.getResponse() != null)
                client.getResponse().close();
        }
    }

    public void checkAuthorization(String authToken, String receiver) {
        RegistryInfoModel registryInfoModel = new RegistryInfoModel();
        registryInfoModel.setReceiver(receiver);
        registryInfoModel.setAuthToken(authToken);
        WebClient client = getWebClient("/service/checkAuthorization");
        Response response = client.post(registryInfoModel);
        if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
            throw new RuntimeException("Participant "
                    + receiver
                    + " is not authorized. "
                    + response.readEntity(String.class));
        }
        try {
            checkResponseError(response);
        } catch (IOException e) {
            logger.error("toString method", e);
        }
    }

    private WebClient getWebClient(String path) {
        return WebClient.create(property.getRegistryAddress() + path,
                Collections.singletonList(provider)).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    }

    private void checkResponseError(Response response) throws IOException {
        if (response.getStatus() != HttpStatus.OK.value()) {
            throw new RuntimeException(messageSource.getMessage("registry.service.error", null, Locale.ENGLISH)
                    + " status:" + response.getStatus() + " from url: " + property.getRegistryAddress() + " : " + IOUtils.toString((InputStream) response.getEntity(), "UTF-8"));
        }
    }

}
