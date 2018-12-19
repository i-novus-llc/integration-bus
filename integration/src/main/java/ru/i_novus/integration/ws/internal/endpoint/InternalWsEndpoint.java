package ru.i_novus.integration.ws.internal.endpoint;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.JAXBException;
import java.io.IOException;

@WebService(targetNamespace = "http://ws.integration.i_novus.ru/internal", name = "InternalWsEndpoint")
public interface InternalWsEndpoint {

    @WebMethod()
    Boolean internal(@WebParam(name = "IntegrationMessage") String message) throws IOException, JAXBException;

    @WebMethod()
    Object[] adapter(@WebParam(name = "Message") String message,
                    @WebParam(name = "RecipientUrl")String recipientUrl, @WebParam(name = "Method")String method) throws Exception;
}
