package ru.i_novus.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.gateway.InboundGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.InputModel;
import ru.i_novus.integration.model.MessageStatusEnum;
import ru.i_novus.integration.rest.client.RegistryClient;
import ru.i_novus.is.integration.common.api.MonitoringModel;
import ru.i_novus.is.integration.common.api.ParticipantModel;

import java.io.IOException;
import java.util.Date;
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

    @GetMapping(path = "/syncRequest/{method}")
    public Object syncRequest(@RequestParam Map<String, String> requestParams, @PathVariable("method") String method) throws IOException {
        MonitoringModel monitoringModel = new MonitoringModel(UUID.randomUUID().toString(), new Date(), property.getEnvCode(),
                requestParams.get("recipient"), "", MessageStatusEnum.CREATE.getId());
        ParticipantModel participantModel = registryClient.getServiceParticipant(requestParams.get("recipient"), property.getEnvCode(), method);
        CommonModel commonModel = new CommonModel();
        commonModel.setParticipantModel(participantModel);
        commonModel.setMonitoringModel(monitoringModel);
        commonModel.setObject(requestParams);

        return inboundGateway.syncRequest(commonModel).getPayload();
    }

    @PostMapping(path = "/aSyncRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public void aSyncRequest(@RequestBody InputModel model) throws IOException {
        MonitoringModel monitoringModel = new MonitoringModel(model.getUid() != null ? model.getUid() +
                "-" + UUID.randomUUID().toString() : UUID.randomUUID().toString(), new Date(),
                property.getEnvCode(), model.getRecipient(), "", MessageStatusEnum.CREATE.getId());
        ParticipantModel participantModel = registryClient.getServiceParticipant(model.getRecipient(), property.getEnvCode(), model.getMethod());
        CommonModel commonModel = new CommonModel();
        commonModel.setParticipantModel(participantModel);
        commonModel.setMonitoringModel(monitoringModel);
        commonModel.setObject(model);

        inboundGateway.aSyncRequest(commonModel);
    }
}
