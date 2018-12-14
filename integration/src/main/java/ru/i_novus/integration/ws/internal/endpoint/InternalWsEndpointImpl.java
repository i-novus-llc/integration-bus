package ru.i_novus.integration.ws.internal.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.configuration.WebApplicationContextLocator;
import ru.i_novus.integration.service.FileStorageService;
import ru.i_novus.integration.ws.internal.model.IntegrationMessage;
import ru.i_novus.integration.ws.internal.client.InternalWsClient;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
import java.io.IOException;

@Service
@WebService(endpointInterface = "ru.i_novus.integration.ws.internal.endpoint.InternalWsEndpoint", serviceName = "InternalWsEndpoint",
        portName = "IntegrationEndpointPort", targetNamespace = "http://ws.integration.i_novus.ru/internal")
@BindingType(value = SOAPBinding.SOAP12HTTP_MTOM_BINDING)
public class InternalWsEndpointImpl implements InternalWsEndpoint {

    @Autowired
    PlaceholdersProperty property;
    @Autowired
    InternalWsClient client;
    @Autowired
    FileStorageService fileStorageService;

    public InternalWsEndpointImpl() {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        WebApplicationContext currentContext = WebApplicationContextLocator.getCurrentWebApplicationContext();
        bpp.setBeanFactory(currentContext.getAutowireCapableBeanFactory());
        bpp.processInjection(this);
    }

    @Override
    public Boolean request(Object message) throws IOException {

        fileStorageService.saveDocumentInStorage(message);
        return true;
    }

    @Override
    public Boolean adapter(Object message, String recipientUrl) {

        return client.sendRequest(message, recipientUrl);
    }


}
