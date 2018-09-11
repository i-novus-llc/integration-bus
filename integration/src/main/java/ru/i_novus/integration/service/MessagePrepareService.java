package ru.i_novus.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.MessageStatusEnum;
import ru.i_novus.integration.model.MonitoringModel;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Component
public class MessagePrepareService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MonitoringGateway monitoringGateway;
    @Autowired
    MessageSource messageSource;

    private static final String VERSIONS = "versions?";
    private static final String VERSIONS_PARAM_NAME = "&version=";
    private static final String DATA = "data?";
    private static final String IDENTIFIER_PARAM_NAME = "identifier=";
    private static final String PAGE = "&page=";

    public Message prepareNsiRequest(Message<CommonModel> message) {
        Map<String, String> messageParam = (Map) message.getPayload().getObject();
        StringBuilder sb = new StringBuilder(messageParam.get("url"));

        if (messageParam.get("method").equals("data")) {
            if (messageParam.get("page").equals("1")) {
                MonitoringModel monitoringModel = message.getPayload().getMonitoringModel();
                monitoringModel.setDateTime(new Date());
                monitoringModel.setStatus(MessageStatusEnum.CREATE.getId());
                monitoringModel.setOperation(messageSource.getMessage("nsi.update.operation", null, Locale.ENGLISH)
                        + messageParam.get("identifier") + VERSIONS_PARAM_NAME + messageParam.get("version"));
                monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringModel).build());
            }
            sb.append("/").append(DATA).append(IDENTIFIER_PARAM_NAME).append(messageParam.get("identifier"))
                    .append(VERSIONS_PARAM_NAME).append(messageParam.get("version")).append(PAGE).append(messageParam.get("page"));
        }

        if (messageParam.get("method").equals("versions")) {
            sb.append("/").append(VERSIONS).append(IDENTIFIER_PARAM_NAME).append(messageParam.get("identifier"));
        }
        return MessageBuilder.withPayload(restTemplate.getForEntity(sb.toString(), String.class).getBody()).build();
    }
}
