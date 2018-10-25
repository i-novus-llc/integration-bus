package ru.i_novus.integration.service;

import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.MessageStatusEnum;
import ru.i_novus.is.integration.common.api.MonitoringModel;
import ru.i_novus.is.integration.common.api.ParticipantModel;

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

    private static final String VERSIONS_PARAM_NAME = "&version=";
    private static final String PARAM = "?";
    private static final String IDENTIFIER_PARAM_NAME = "identifier=";
    private static final String PAGE = "&page=";

    public Message prepareNsiRequest(Message<CommonModel> message) {
        Map<String, String> messageParam = (Map) message.getPayload().getObject();
        ParticipantModel participantModel = message.getPayload().getParticipantModel();

        StringBuilder sb = new StringBuilder(participantModel.getUrl());

        if (participantModel.getMethod().equals("data")) {
            if (messageParam.get("page").equals("1")) {
                monitoringMessage(participantModel.getMethod(), message.getPayload().getMonitoringModel(), messageParam.get("identifier"),
                        messageParam.get("version"), MessageStatusEnum.CREATE.getId());
            }
            sb.append(PARAM).append(IDENTIFIER_PARAM_NAME).append(messageParam.get("identifier"))
                    .append(VERSIONS_PARAM_NAME).append(messageParam.get("version")).append(PAGE).append(messageParam.get("page"));
        }

        if (participantModel.getMethod().equals("versions")) {
            sb.append(PARAM).append(IDENTIFIER_PARAM_NAME).append(messageParam.get("identifier"));
        }
        Message result = MessageBuilder.withPayload(restTemplate.getForEntity(sb.toString(), String.class).getBody()).build();

        if (new JsonParser().parse(result.getPayload().toString()).getAsJsonObject().get("list").toString().equals("[]")) {
            monitoringMessage(participantModel.getMethod(), message.getPayload().getMonitoringModel(), messageParam.get("identifier"),
                    messageParam.get("version"), MessageStatusEnum.SEND.getId());
        }

        return result;
    }

    private void monitoringMessage(String operation,MonitoringModel monitoringModel, String identifier, String version, int status){
        monitoringModel.setDateTime(new Date());
        monitoringModel.setStatus(status);
        monitoringModel.setOperation(operation);
        monitoringModel.setComment(messageSource.getMessage("nsi.update.operation", null, Locale.ENGLISH) +
                identifier + messageSource.getMessage("nsi.update.operation.version", null, Locale.ENGLISH) + version);
        monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringModel).build());
    }
}
