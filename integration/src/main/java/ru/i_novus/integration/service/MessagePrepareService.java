package ru.i_novus.integration.service;

import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.MessageStatusEnum;
import ru.i_novus.integration.common.api.MonitoringModel;
import ru.i_novus.integration.common.api.ParticipantModel;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;

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
        ResponseEntity<Object> responseEntity = null;
        try {
            if (participantModel.getIntegrationType().equals("REST_GET")) {
                responseEntity = restTemplate.getForEntity(participantModel.getUrl(), Object.class);
                message = MessageBuilder.withPayload(responseEntity.getBody()).build();

                checkError(responseEntity, messageCommonModel);
                result = responseEntity.getBody();

                if (participantModel.getMethod().equals("nsi") &&
                        new JsonParser().parse(result.toString()).getAsJsonObject().get("list").toString().equals("[]")) {
                    monitoringNsiMessage(participantModel.getMethod(),
                            messageCommonModel.getPayload().getMonitoringModel(), MessageStatusEnum.SEND.getId());
                }
            }
            if (participantModel.getIntegrationType().equals("REST_POST")) {
                responseEntity = (ResponseEntity<Object>) restTemplate.postForObject(participantModel.getUrl(),
                        messageCommonModel.getPayload().getObject(), Object.class);

                checkError(responseEntity, messageCommonModel);
                if (participantModel.isSync() && result != null) {
                    message = MessageBuilder.withPayload(result).build();
                } else {
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
        monitoringRequestMessage(participantModel.getMethod(), messageCommonModel.getPayload().getMonitoringModel(), MessageStatusEnum.SEND.getId());

        return message;
    }

    private void checkError(ResponseEntity responseEntity, Message<CommonModel> modelMessage) {
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            monitoringGateway.createError(MessageBuilder.withPayload(modelMessage.getPayload().getMonitoringModel()).build());

            throw new RuntimeException(responseEntity.getBody().toString());
        }
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
    private void monitoringNsiMessage(String operation, MonitoringModel monitoringModel, int status) {
        monitoringRequestMessage(operation, monitoringModel, status);
        monitoringModel.setComment(messageSource.getMessage("nsi.update.operation", null, Locale.ENGLISH) +
                messageSource.getMessage("nsi.update.operation.version", null, Locale.ENGLISH));
        monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringModel).build());
    }

    /**
     * Заполнение мониторинга для отправки
     * @param operation выполненная операция
     * @param monitoringModel модель
     * @param status статус передачи
     */
    private void monitoringRequestMessage(String operation, MonitoringModel monitoringModel, int status) {
        monitoringModel.setDateTime(new Date());
        monitoringModel.setStatus(status);
        monitoringModel.setOperation(operation);
    }
}
