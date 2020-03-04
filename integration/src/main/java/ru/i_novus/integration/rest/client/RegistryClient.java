package ru.i_novus.integration.rest.client;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class RegistryClient {
    private final IntegrationProperties property;
    private final MessageSource messageSource;

    @Autowired
    public RegistryClient(IntegrationProperties property, MessageSource messageSource) {
        this.property = property;
        this.messageSource = messageSource;
    }

    public ParticipantModel getServiceParticipant(String receiver, String sender, String  method) throws IOException {
        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJsonProvider());

        RegistryInfoModel registryInfoModel = new RegistryInfoModel();
        registryInfoModel.setSender(sender);
        registryInfoModel.setReceiver(receiver);
        registryInfoModel.setMethod(method);
        WebClient client = WebClient.create(property.getRegistryAddress() + "/service/prepareRequest", providers)
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

        try {
            Response response = client.post(registryInfoModel);
            checkResponseError(response);
            return response.readEntity(ParticipantModel.class);
        } finally {
            if (client.getResponse() != null)
                client.getResponse().close();
        }
    }

    private void checkResponseError(Response response) throws IOException {
        if (response.getStatus() != HttpStatus.OK.value()) {
            throw new RuntimeException(messageSource.getMessage("registry.service.error", null, Locale.ENGLISH)
                    + " status:" + response.getStatus() + " from url: " + property.getRegistryAddress() + " : " + IOUtils.toString((InputStream) response.getEntity(), "UTF-8"));
        }
    }

}
