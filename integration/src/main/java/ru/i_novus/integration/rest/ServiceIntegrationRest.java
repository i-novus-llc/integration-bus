package ru.i_novus.integration.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.gateway.InboundGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.InternalRequestModel;
import ru.i_novus.integration.model.RequestModel;
import ru.i_novus.integration.service.CommonModelPrepareService;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(path = "/get/{service}/{method}/**")
    public Object sync(HttpServletRequest request, @PathVariable("service") String service, @PathVariable("method") String method,
                       @RequestParam Map<String, String> requestParams) throws IOException {

        CommonModel commonModel = modelPrepareService.getRequestModelPrepare(requestParams, method, service);
        String url = commonModel.getParticipantModel().getUrl() + request.getRequestURI()
                .replace("/integration/get/" + service + "/" + method, "") + "?" +request.getQueryString();
        commonModel.getParticipantModel().setUrl(url);
        if (commonModel.getParticipantModel().isSync()) {
            return inboundGateway.syncRequest(commonModel).getPayload();
        } else {
            inboundGateway.aSyncRequest(commonModel);
        }

        return HttpStatus.ACCEPTED;
    }

    @PostMapping(path = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object post(@RequestBody RequestModel model) throws IOException {
        CommonModel commonModel = modelPrepareService.requestModelPreparation(model);
        if (commonModel.getParticipantModel().isSync()) {
            return inboundGateway.syncRequest(commonModel).getPayload();
        } else {
            inboundGateway.aSyncRequest(commonModel);
        }

        return HttpStatus.ACCEPTED;
    }

    @GetMapping(path = "/syncRequest/{method}")
    public Object syncRequest(@RequestParam Map<String, String> requestParams, @PathVariable("method") String method) throws IOException {

        return inboundGateway.syncRequest(modelPrepareService.getRequestModelPrepare(requestParams, method, requestParams.get("recipient"))).getPayload();
    }

    @PostMapping(path = "/aSyncRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public void aSyncRequest(@RequestBody InternalRequestModel model) throws IOException {

        inboundGateway.internalRequest(modelPrepareService.requestModelPreparation(model));
    }
}
