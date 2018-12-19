package ru.i_novus.integration.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.i_novus.integration.ws.internal.endpoint.InternalWsEndpointImpl;

import javax.xml.ws.Endpoint;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class InternalWSConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private PlaceholdersProperty property;

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, new InternalWsEndpointImpl());
        endpoint.publish("/internal");
        //endpoint.setHandlers(Collections.singletonList(new SignatureSOAPHandler(property.getKeyStore(), property.getKeyStoreAlias(), property.getKeyStoreAliasPassword())));
        Map<String, Object> props = new HashMap<>();
        //props.put("mtom-enabled", Boolean.TRUE);
        endpoint.setProperties(props);
        return endpoint;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        restTemplate.getMessageConverters().add(converter);

        return restTemplate;
    }
}
