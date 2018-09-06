package ru.i_novus.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.gateway.InboundGateway;
import ru.i_novus.integration.model.*;
import ru.i_novus.integration.rest.client.RegistryClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
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

    @Autowired
    PlaceholdersProperty property;

    @GetMapping(path = "/syncRequest")
    public Object syncRequest(@RequestParam Map<String, String> requestParams, @RequestHeader HttpHeaders headers) throws IOException {
        return inboundGateway.syncRequest(MessageBuilder.createMessage(requestParams,
                new MonitoringHeaderModel(new HashMap<>(), UUID.randomUUID().toString(),
                        requestParams.get("receiver"), LocalDateTime.now(), property.getEnvCode(),
                        MessageStatusEnum.CREATE.getId()))).getPayload();
    }

    @PostMapping(path = "/aSyncRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public void aSyncRequest(@RequestBody InputModel model, @RequestHeader HttpHeaders headers) throws IOException {
        MonitoringModel monitoringModel = new MonitoringModel(UUID.randomUUID().toString(), new Date(), model.getRecipient(),
                property.getEnvCode(), "", MessageStatusEnum.CREATE.getId());
        CommonModel commonModel = new CommonModel();
        commonModel.setMonitoringModel(monitoringModel);
        commonModel.setObject(model);

        inboundGateway.aSyncRequest(commonModel);
    }
}
