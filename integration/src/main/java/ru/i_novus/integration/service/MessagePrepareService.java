package ru.i_novus.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class MessagePrepareService {
    @Autowired
    RestTemplate restTemplate;

    private static final String VERSIONS = "versions?";
    private static final String VERSIONS_PARAM_NAME = "&version=";
    private static final String DATA = "data?";
    private static final String IDENTIFIER_PARAM_NAME = "identifier=";
    private static final String PAGE = "&page=";

    public Message prepareNsiRequest(Message message) {
        Map<String, String> messageParam = (Map) message.getPayload();
        StringBuilder sb = new StringBuilder((String) messageParam.get("url"));

        if (messageParam.get("method").equals("versions")) {
            sb.append("/").append(VERSIONS).append(IDENTIFIER_PARAM_NAME).append(messageParam.get("identifier"))
            .append(VERSIONS_PARAM_NAME).append(messageParam.get("version")).append(PAGE).append(messageParam.get("page"));
        }

        if (messageParam.get("method").equals("data")) {
            sb.append("/").append(DATA).append(IDENTIFIER_PARAM_NAME).append(messageParam.get("identifier"));
        }
        return MessageBuilder.withPayload(restTemplate.getForEntity(sb.toString(), String.class).getBody()).build();
    }
}
