package ru.i_novus.integration.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.gateway.InboundGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.InternalRequestModel;
import ru.i_novus.integration.model.RequestModel;
import ru.i_novus.integration.rest.client.RegistryClient;
import ru.i_novus.integration.service.CommonModelPrepareService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/")
public class ServiceIntegrationRest {

    private final InboundGateway inboundGateway;

    private final CommonModelPrepareService modelPrepareService;

    private final RegistryClient registryClient;

    public ServiceIntegrationRest(InboundGateway inboundGateway, CommonModelPrepareService modelPrepareService,
                                  RegistryClient registryClient) {
        this.inboundGateway = inboundGateway;
        this.modelPrepareService = modelPrepareService;
        this.registryClient = registryClient;
    }

    /**
     * Обработка get запросов
     *
     * @param request       http request
     * @param service       зарегистрированный код получателя
     * @param method        код зарегистрированного метода получателя
     * @param requestParams параметры path, query
     * @return возвращает Object если запрос синхронный
     * @throws IOException
     */
    @GetMapping(path = "/get/{service}/{method}/**")
    public Object get(HttpServletRequest request, @PathVariable("service") String service, @PathVariable("method") String method,
                      @RequestParam Map<String, String> requestParams) throws IOException {
        Enumeration<String> authHeaders = request.getHeaders("Authorization");
        registryClient.checkAuthorization(getSpecificHeader(authHeaders, "Bearer"), service);
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", getSpecificHeader(authHeaders, "Basic"));
        CommonModel commonModel = modelPrepareService.getRequestModelPrepare(requestParams, method, service);
        String url = commonModel.getParticipantModel().getUrl() +
                (request.getRequestURI().replace("/integration/get/" + service + "/" + method, "")) +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        commonModel.getParticipantModel().setUrl(url);
        if (commonModel.getParticipantModel().isSync()) {
            Message result = inboundGateway.syncRequest(commonModel, headers);
            return result == null ? null : result.getPayload();
        } else {
            inboundGateway.aSyncRequest(commonModel, headers);
        }

        return HttpStatus.ACCEPTED;
    }

    /**
     * Обработка POST заросов
     *
     * @param model модель
     * @return возвращает Object если запрос синхронный
     * @throws IOException
     */
    @PostMapping(path = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> post(HttpServletRequest request, @RequestBody RequestModel model) {
        Enumeration<String> authHeaders = request.getHeaders("Authorization");
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", getSpecificHeader(authHeaders, "Basic"));
        model.getRecipient().forEach(recipient ->
                registryClient.checkAuthorization(getSpecificHeader(authHeaders, "Bearer"), recipient));
        List<Object> result = new ArrayList<>();
        modelPrepareService.requestModelPreparation(model)
                .parallelStream()
                .forEach(commonModel -> {
                    if (commonModel.getParticipantModel().isSync()) {
                        result.add(inboundGateway.syncRequest(commonModel, headers).getPayload());
                    } else {
                        inboundGateway.aSyncRequest(commonModel, headers);
                        result.add(HttpStatus.OK);
                    }
                });

        return result;
    }

    /**
     * @deprecated замнен на метод get
     */
    @GetMapping(path = "/syncRequest/{method}")
    public Object syncRequest(@RequestParam Map<String, String> requestParams, @PathVariable("method") String method) throws IOException {

        return inboundGateway.syncRequest(modelPrepareService.getRequestModelPrepare(requestParams, method, requestParams.get("recipient")), new HashMap<>()).getPayload();
    }

    /**
     * Передача файлов между ИШ
     *
     * @param model модель с описанием пердаваемого файла
     * @throws IOException
     */
    @PostMapping(path = "/aSyncRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public void internalRequest(HttpServletRequest request, @RequestBody InternalRequestModel model) throws IOException {
        Enumeration<String> authHeaders = request.getHeaders("Authorization");
        registryClient.checkAuthorization(getSpecificHeader(authHeaders, "Bearer"), model.getRecipient());
        inboundGateway.internalRequest(modelPrepareService.requestModelPreparation(model));
    }

    private String getSpecificHeader(Enumeration<String> headers, String tokenType) {
        Iterator<String> iterator = headers.asIterator();
        while (iterator.hasNext()) {
            String header = iterator.next();
            String[] headerParts = header.split(" ");
            if (headerParts.length > 0) {
                if (headerParts[0].equals(tokenType)) {
                    return header;
                }
            }
        }
        return "";
    }
}
