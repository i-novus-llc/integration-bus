package ru.i_novus.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.gateway.InboundGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.InputModel;
import ru.i_novus.integration.rest.client.RegistryClient;
import ru.i_novus.integration.service.MonitoringService;
import ru.i_novus.is.integration.common.api.ParticipantModel;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/integration")
public class ServiceIntegrationRest {

    @Autowired
    InboundGateway inboundGateway;

    @Autowired
    RegistryClient registryClient;

    @Autowired
    PlaceholdersProperty property;

    @Autowired
    MonitoringService monitoringService;

    @GetMapping(path = "/syncRequest/{method}")
    public Object syncRequest(@RequestParam Map<String, String> requestParams, @PathVariable("method") String method) throws IOException {

        requestParams.put("method", method);
        ParticipantModel participantModel = registryClient.getServiceParticipant(requestParams.get("recipient"), property.getEnvCode(), method);
        CommonModel commonModel = new CommonModel();
        commonModel.setParticipantModel(participantModel);
        commonModel.setMonitoringModel(monitoringService.prepareModel(requestParams));
        commonModel.setObject(requestParams);

        return inboundGateway.syncRequest(commonModel).getPayload();
    }

    @PostMapping(path = "/aSyncRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public void aSyncRequest(@RequestBody InputModel model) throws IOException {

        ParticipantModel participantModel = registryClient.getServiceParticipant(model.getRecipient(), property.getEnvCode(), model.getMethod());
        CommonModel commonModel = new CommonModel();
        commonModel.setParticipantModel(participantModel);
        commonModel.setMonitoringModel(monitoringService.prepareModel(model));
        commonModel.setObject(model);

        inboundGateway.aSyncRequest(commonModel);
    }
}
