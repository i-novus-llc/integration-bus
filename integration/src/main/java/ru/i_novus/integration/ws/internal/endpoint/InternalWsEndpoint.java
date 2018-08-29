package ru.i_novus.integration.ws.internal.endpoint;

import ru.i_novus.integration.ws.internal.IntegrationMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.IOException;

@WebService(targetNamespace = "http://ws.integration.i_novus.ru/internal", name = "InternalWsEndpoint")
public interface InternalWsEndpoint {

    @WebMethod()
    Boolean request(@WebParam(name = "IntegrationMessage") IntegrationMessage message) throws IOException;
}