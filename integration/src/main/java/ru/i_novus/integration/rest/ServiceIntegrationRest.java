package ru.i_novus.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.InboundGateway;
import ru.i_novus.integration.model.InputModel;

import java.util.Map;

@RestController
@RequestMapping("/integration")
public class ServiceIntegrationRest {

    @Autowired
    InboundGateway inboundGateway;

    @GetMapping(path = "/syncRequest")
    public Object syncRequest(@RequestParam Map<String, String> requestParams) {
        return inboundGateway.syncRequest(MessageBuilder.withPayload(requestParams).build()).getPayload();
    }

    @PostMapping(path = "/aSyncRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public void aSyncRequest(@RequestBody InputModel model) {
        inboundGateway.aSyncRequest(model);
    }
}
