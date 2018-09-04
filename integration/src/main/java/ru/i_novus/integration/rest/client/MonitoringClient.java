package ru.i_novus.integration.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.model.MonitoringModel;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Component
public class MonitoringClient {
    @Autowired
    PlaceholdersProperty property;
    @Autowired
    MessageSource messageSource;

    public void sendMonitoringMessage(MonitoringModel model) throws IOException {
        List<Object> providers = new ArrayList<Object>();
        providers.add(new JacksonJsonProvider());

        WebClient client = WebClient.create(property.getMonitoringAddress() + "/service/save", providers)
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        try {
            Response response = client.post(model);
            checkResponseError(response);
            //return IOUtils.toString((InputStream) response.getEntity(), "UTF-8");
        } finally {
            if (client.getResponse() != null)
                client.getResponse().close();
        }
    }


    private void checkResponseError(Response response) throws IOException {
        if (response.getStatus() != HttpStatus.OK.value()) {
            throw new RuntimeException(messageSource.getMessage("registry.service.error", null, Locale.ENGLISH)
                    , new Throwable((String) new ObjectMapper()
                    .readValue(IOUtils.toString((InputStream) response.getEntity(), "UTF-8"), HashMap.class).get("message")));
        }
    }
}
