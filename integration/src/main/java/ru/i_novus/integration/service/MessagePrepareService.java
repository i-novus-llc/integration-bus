package ru.i_novus.integration.service;

import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.MessageStatusEnum;
import ru.i_novus.is.integration.common.api.MonitoringModel;
import ru.i_novus.is.integration.common.api.ParticipantModel;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Component
public class MessagePrepareService {
    private final RestTemplate restTemplate;
    private final MonitoringGateway monitoringGateway;
    private final MessageSource messageSource;

    @Autowired
    public MessagePrepareService(RestTemplate restTemplate, MonitoringGateway monitoringGateway, MessageSource messageSource) {
        this.restTemplate = restTemplate;
        this.monitoringGateway = monitoringGateway;
        this.messageSource = messageSource;
    }

    /**
     * Синхронный запрос
     * @param messageCommonModel модель для запроса
     * @return возвращает Message c обьектом ответа
     */
    public Message prepareSyncRequest(Message<CommonModel> messageCommonModel) {
        return prepareRequest(messageCommonModel);
    }

    /**
     * Асинхронный запрос
     * @param messageCommonModel модель для запроса
     */
    public void prepareAsyncRequest(Message<CommonModel> messageCommonModel) {
        prepareRequest(messageCommonModel);
    }

    /**
     * Подготовка и передача запроса получателю
     * @param messageCommonModel модель для запроса
     * @return если вызов синхронный вернет Message c обьектом ответа иначе вернет SUCCESS
     */
    private Message prepareRequest(Message<CommonModel> messageCommonModel) {
        ParticipantModel participantModel = messageCommonModel.getPayload().getParticipantModel();

        Message message = null;
        Object result = null;
        try {
            if (participantModel.getIntegrationType().equals("REST_GET")) {
                result = restTemplate.getForEntity(participantModel.getUrl(), String.class).getBody();
                message = MessageBuilder.withPayload(result).build();

                if (participantModel.getMethod().equals("nsi") && new JsonParser().parse(message.getPayload().toString()).getAsJsonObject().get("list").toString().equals("[]")) {
                    monitoringMessage(participantModel.getMethod(), messageCommonModel.getPayload().getMonitoringModel(), MessageStatusEnum.SEND.getId());
                }
            }
            if (participantModel.getIntegrationType().equals("REST_POST")) {
                if (participantModel.isSync()) {
                    message = MessageBuilder.withPayload(restTemplate.postForObject(participantModel.getUrl(),
                            messageCommonModel.getPayload().getObject(), Object.class)).build();
                } else {
                    result = restTemplate.postForLocation(participantModel.getUrl(),
                            messageCommonModel.getPayload().getObject(), Object.class);
                    message = MessageBuilder.withPayload("SUCCESS").build();
                }
            }
            if (participantModel.getIntegrationType().equals("SOAP")) {
                WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
                webServiceTemplate.setDefaultUri(participantModel.getUrl());

                StreamSource source = new StreamSource(new StringReader((String) messageCommonModel.getPayload().getObject()));
                StringWriter writer = new StringWriter();
                StreamResult xmlResult = new StreamResult(writer);
                webServiceTemplate.sendSourceAndReceiveToResult(source, xmlResult);

                result = writer.toString();
            }

            if (participantModel.getCallbackUrl() != null) {
                sendCallBack(participantModel.getCallbackUrl(), result);
            }
        } catch (Exception ex) {
            throw new RuntimeException(participantModel.getUrl(), ex);
        }

        return message;
    }

    /**
     * Вызов сервиса для callBack
     * @param url callBackUrl
     * @param response Object для передачи
     */
    private void sendCallBack(String url, Object response) {
        restTemplate.postForLocation(url, response);
    }

    /**
     * Заполнение мониторинга для NSI
     * @param operation выполненная операция
     * @param monitoringModel модель
     * @param status статус передачи
     */
    private void monitoringMessage(String operation, MonitoringModel monitoringModel, int status) {
        monitoringModel.setDateTime(new Date());
        monitoringModel.setStatus(status);
        monitoringModel.setOperation(operation);
        monitoringModel.setComment(messageSource.getMessage("nsi.update.operation", null, Locale.ENGLISH) +
                messageSource.getMessage("nsi.update.operation.version", null, Locale.ENGLISH));
        monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringModel).build());
    }
}
