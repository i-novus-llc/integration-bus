package ru.i_novus.integration.ws.internal.client;

import org.apache.cxf.jaxws.JaxWsClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.i_novus.common.sign.soap.SignatureSOAPHandler;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.ws.internal.model.IOException_Exception;
import ru.i_novus.integration.ws.internal.model.IntegrationMessage;
import ru.i_novus.integration.ws.internal.model.InternalWsEndpoint;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPBinding;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InternalWsClient {

    @Autowired
    PlaceholdersProperty property;

    public void sendRequest(Message<IntegrationMessage> request) throws IOException_Exception {
        InternalWsEndpoint integrationEndpoint = getPort();
        BindingProvider bindingProvider = (BindingProvider) integrationEndpoint;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, request.getHeaders().get("url", String.class));
        integrationEndpoint.request(request.getPayload());
    }

    private InternalWsEndpoint getPort(){
        InternalWsEndpoint port;
        Map<String, Object> props = new HashMap<>();
        props.put("mtom-enabled", Boolean.TRUE);
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(InternalWsEndpoint.class);
        factory.setProperties(props);
        port = ((InternalWsEndpoint) factory.create());

        HTTPConduit conduit = (HTTPConduit) JaxWsClientProxy.getClient(port).getConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setAutoRedirect(true);
        policy.setReceiveTimeout(300000L);
        conduit.setClient(policy);

        KeyStore store;

        try {
            store = KeyStore.getInstance("JKS");
            store.load(new ClassPathResource("keystore").getInputStream(), property.getKeyStorePassword().toCharArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        bindingSOAPHandler(port,
                new SignatureSOAPHandler(store, property.getKeyStoreAlias(), property.getKeyStoreAliasPassword()));

        return port;
    }

    private void bindingSOAPHandler(Object port, SOAPHandler<SOAPMessageContext> soapHandler) {
        BindingProvider provider = (BindingProvider)port;
        Binding binding = provider.getBinding();
        ((SOAPBinding)binding).setMTOMEnabled(true);
        List<Handler> handlerChain = binding.getHandlerChain();
        handlerChain.add(soapHandler);
        binding.setHandlerChain(handlerChain);
    }
}
