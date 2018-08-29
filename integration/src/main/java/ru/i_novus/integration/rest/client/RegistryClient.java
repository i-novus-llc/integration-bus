package ru.i_novus.integration.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.PlaceholdersProperty;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class RegistryClient {
    @Autowired
    PlaceholdersProperty property;
    @Autowired
    MessageSource messageSource;

    public String getServiceUrlByCode(String code) throws IOException {
        WebClient client = WebClient
                .fromClient(WebClient.create(property.getRegistryAddress())
                        .accept(MediaType.APPLICATION_JSON))
                .replacePath("/service/info/" + code);
        try {
            Response response = client.get();
            checkResponseError(response, client);
            return IOUtils.toString((InputStream) response.getEntity(), "UTF-8");
        } finally {
            if (client.getResponse() != null)
                client.getResponse().close();
        }
    }

    public String getServiceCodeByHost(String host) throws IOException {
        WebClient client = WebClient
                .fromClient(WebClient.create(property.getRegistryAddress())
                        .accept(MediaType.APPLICATION_JSON))
                .replacePath("/service/info/url" + host);
        try {
            Response response = client.get();
            checkResponseError(response, client);
            return IOUtils.toString((InputStream) response.getEntity(), "UTF-8");
        } finally {
            if (client.getResponse() != null)
                client.getResponse().close();
        }
    }

    public Message getServiceUrl(Message message) throws IOException {
        Map<String, String> messageParam = (Map) message.getPayload();
        WebClient client = WebClient
                .fromClient(WebClient.create(property.getRegistryAddress())
                        .accept(MediaType.APPLICATION_JSON))
                .replacePath("/service/info/" + messageParam.get("recipient"));
        try {
            Response response = client.get();
            checkResponseError(response, client);
            messageParam.put("url", IOUtils.toString((InputStream) response.getEntity(), "UTF-8"));

            return message;
        } finally {
            if (client.getResponse() != null)
                client.getResponse().close();
        }
    }

    private void checkResponseError(Response response, WebClient client) throws IOException {
        if (response.getStatus() != HttpStatus.OK.value()) {
            throw new RuntimeException(messageSource.getMessage("registry.service.error", null, Locale.ENGLISH)
                    , new Throwable((String) new ObjectMapper()
                    .readValue(IOUtils.toString((InputStream) client.get().getEntity(), "UTF-8"), HashMap.class).get("message")));
        }
    }

}
