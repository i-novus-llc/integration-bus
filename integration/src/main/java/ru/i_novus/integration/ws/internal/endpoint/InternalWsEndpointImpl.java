package ru.i_novus.integration.ws.internal.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ru.i_novus.integration.configuration.WebApplicationContextLocator;
import ru.i_novus.integration.service.FileService;

import javax.jws.WebService;
import javax.xml.bind.JAXBException;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
import java.io.IOException;

@Service
@WebService(endpointInterface = "ru.i_novus.integration.ws.internal.endpoint.InternalWsEndpoint", serviceName = "InternalWsEndpoint",
        portName = "IntegrationEndpointPort", targetNamespace = "http://ws.integration.i_novus.ru/internal")
@BindingType(value = SOAPBinding.SOAP12HTTP_MTOM_BINDING)
public class InternalWsEndpointImpl implements InternalWsEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(InternalWsEndpointImpl.class);

    @Autowired
    private FileService fileService;

    public InternalWsEndpointImpl() {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        WebApplicationContext currentContext = WebApplicationContextLocator.getCurrentWebApplicationContext();
        bpp.setBeanFactory(currentContext.getAutowireCapableBeanFactory());
        bpp.processInjection(this);
    }

    @Override
    public Boolean internal(String message) throws IOException, JAXBException {
        logger.info("receive 'internal' call, message.length {}", message.length());
        fileService.saveDocumentInStorage(message);
        logger.info("'internal' call completed");
        return true;
    }

    @Override
    @Deprecated
    public Object[] adapter(String message, String recipientUrl, String method) {
        throw new UnsupportedOperationException();
    }
}
