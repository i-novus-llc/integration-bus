package ru.i_novus.integration.ws.internal.endpoint;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.IOException;

@WebService(targetNamespace = "http://ws.integration.i_novus.ru/internal", name = "InternalWsEndpoint")
public interface InternalWsEndpoint {

    @WebMethod()
    Boolean request(@WebParam(name = "IntegrationMessage") Object message) throws IOException;

    @WebMethod()
    Boolean adapter(@WebParam(name = "IntegrationMessage") Object message,
                    @WebParam(name = "RecipientUrl")String recipientUrl) throws IOException;
}
