package ru.i_novus.integration.rest.client;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.is.integration.common.api.MonitoringModel;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class MonitoringClient {
    @Autowired
    PlaceholdersProperty property;
    @Autowired
    MessageSource messageSource;

    public void sendMonitoringMessage(MonitoringModel model) throws IOException {
        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJsonProvider());

        WebClient client = WebClient.create(property.getMonitoringAddress() + "/service/save", providers)
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
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
