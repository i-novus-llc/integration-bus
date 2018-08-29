package ru.i_novus.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.gateway.InboundGateway;
import ru.i_novus.integration.model.InputModel;
import ru.i_novus.integration.model.MessageStatusEnum;
import ru.i_novus.integration.model.MonitoringHeaderModel;
import ru.i_novus.integration.rest.client.RegistryClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/integration")
public class ServiceIntegrationRest {

    @Autowired
    InboundGateway inboundGateway;

    @Autowired
    RegistryClient registryClient;

    @GetMapping(path = "/syncRequest")
    public Object syncRequest(@RequestParam Map<String, String> requestParams, @RequestHeader HttpHeaders headers) throws IOException {
        return inboundGateway.syncRequest(MessageBuilder.createMessage(requestParams,
                new MonitoringHeaderModel(new HashMap<>(), UUID.randomUUID().toString(),
                        requestParams.get("receiver"), LocalDateTime.now(),
                        registryClient.getServiceCodeByHost(headers.getHost().getHostString()), MessageStatusEnum.CREATE.getId()))).getPayload();
    }

    @PostMapping(path = "/aSyncRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public void aSyncRequest(@RequestBody InputModel model, @RequestHeader HttpHeaders headers) throws IOException {
        inboundGateway.aSyncRequest(MessageBuilder.createMessage(model,
                new MonitoringHeaderModel(new HashMap<>(), UUID.randomUUID().toString(),
                        model.getRecipient(), LocalDateTime.now(),
                        registryClient.getServiceCodeByHost(headers.getHost().getHostString()), MessageStatusEnum.CREATE.getId())));
    }
}
