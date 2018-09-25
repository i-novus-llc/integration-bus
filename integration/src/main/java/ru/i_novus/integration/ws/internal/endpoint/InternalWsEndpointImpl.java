package ru.i_novus.integration.ws.internal.endpoint;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.configuration.WebApplicationContextLocator;
import ru.i_novus.integration.ws.internal.DocumentData;
import ru.i_novus.integration.ws.internal.IntegrationMessage;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
@WebService(endpointInterface = "ru.i_novus.integration.ws.internal.endpoint.InternalWsEndpoint", serviceName = "InternalWsEndpoint",
        portName = "IntegrationEndpointPort", targetNamespace = "http://ws.integration.i_novus.ru/internal")
@BindingType(value = SOAPBinding.SOAP12HTTP_MTOM_BINDING)
public class InternalWsEndpointImpl implements InternalWsEndpoint {

    @Autowired
    PlaceholdersProperty property;

    public InternalWsEndpointImpl() {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        WebApplicationContext currentContext = WebApplicationContextLocator.getCurrentWebApplicationContext();
        bpp.setBeanFactory(currentContext.getAutowireCapableBeanFactory());
        bpp.processInjection(this);
    }

    @Override
    public Boolean request(IntegrationMessage message) throws IOException {

        saveDocumentInStorage(message.getMessage().getAppData());
        return true;
    }

    private void saveDocumentInStorage(List<DocumentData> list) throws IOException {
        for (DocumentData data : list) {
            try (OutputStream outputStream = new FileOutputStream(new File(property.getTempPath() + "/" + data.getDocName()))) {
                IOUtils.copy(data.getBinaryData().getInputStream(), outputStream);
            }
        }
    }
}
