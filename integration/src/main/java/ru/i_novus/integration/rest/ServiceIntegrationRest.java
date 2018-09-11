package ru.i_novus.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.configuration.PlaceholdersProperty;
import ru.i_novus.integration.gateway.InboundGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.InputModel;
import ru.i_novus.integration.model.MessageStatusEnum;
import ru.i_novus.integration.model.MonitoringModel;
import ru.i_novus.integration.rest.client.RegistryClient;

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

    @GetMapping(path = "/syncRequest")
    public Object syncRequest(@RequestParam Map<String, String> requestParams) {
        MonitoringModel monitoringModel = new MonitoringModel(UUID.randomUUID().toString(), new Date(), property.getEnvCode(),
                requestParams.get("recipient"), "", MessageStatusEnum.CREATE.getId());
        CommonModel commonModel = new CommonModel();
        commonModel.setMonitoringModel(monitoringModel);
        commonModel.setObject(requestParams);

        return inboundGateway.syncRequest(commonModel).getPayload();
    }

    @PostMapping(path = "/aSyncRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public void aSyncRequest(@RequestBody InputModel model) {
        MonitoringModel monitoringModel = new MonitoringModel(UUID.randomUUID().toString(), new Date(),
                property.getEnvCode(), model.getRecipient(), "", MessageStatusEnum.CREATE.getId());
        CommonModel commonModel = new CommonModel();
        commonModel.setMonitoringModel(monitoringModel);
        commonModel.setObject(model);

        inboundGateway.aSyncRequest(commonModel);
    }
}
