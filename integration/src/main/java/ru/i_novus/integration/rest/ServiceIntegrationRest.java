package ru.i_novus.integration.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.gateway.InboundGateway;
import ru.i_novus.integration.model.InputModel;
import ru.i_novus.integration.service.CommonModelPrepareService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/integration")
public class ServiceIntegrationRest {

    private final InboundGateway inboundGateway;

    private final CommonModelPrepareService modelPrepareService;

    public ServiceIntegrationRest(InboundGateway inboundGateway, CommonModelPrepareService modelPrepareService) {
        this.inboundGateway = inboundGateway;
        this.modelPrepareService = modelPrepareService;
    }

    @GetMapping(path = "/syncRequest/{method}")
    public Object syncRequest(@RequestParam Map<String, String> requestParams, @PathVariable("method") String method) throws IOException {

        return inboundGateway.syncRequest(modelPrepareService.syncRequestModelPrepare(requestParams, method)).getPayload();
    }

    @PostMapping(path = "/aSyncRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public void aSyncRequest(@RequestBody InputModel model) throws IOException {

        inboundGateway.aSyncRequest(modelPrepareService.aSyncRequestModelPreparation(model));
    }
}
