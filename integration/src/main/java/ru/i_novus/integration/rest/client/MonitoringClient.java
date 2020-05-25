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
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.configuration.IntegrationProperties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Locale;

@Component
public class MonitoringClient {
    private static final Logger logger = LoggerFactory.getLogger(MonitoringClient.class);
    private final IntegrationProperties property;
    private final MessageSource messageSource;
    private final JacksonJsonProvider provider;

    @Autowired
    public MonitoringClient(IntegrationProperties property, MessageSource messageSource, JacksonJsonProvider provider) {
        this.property = property;
        this.messageSource = messageSource;
        this.provider = provider;
    }

    public void sendMonitoringMessage(MonitoringModel model) throws IOException {
        WebClient client = WebClient.create(property.getMonitoringAddress() + "/service/save",
                Collections.singletonList(provider)).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        try {
            Response response = client.post(model);
            checkResponseError(response);
        } finally {
            if (client.getResponse() != null)
                client.getResponse().close();
        }
    }


    private void checkResponseError(Response response) throws IOException {
        if (response.getStatus() != HttpStatus.OK.value()) {
            throw new RuntimeException(messageSource.getMessage("monitoring.service.error", null, Locale.ENGLISH)
                    + response.getStatus() + " : " + IOUtils.toString((InputStream) response.getEntity(), "UTF-8"));
        }
    }
}
