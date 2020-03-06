package ru.i_novus.integration.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.common.api.model.ParticipantModel;
import ru.i_novus.integration.gateway.MonitoringGateway;
import ru.i_novus.integration.model.CommonModel;
import ru.i_novus.integration.model.MessageStatusEnum;
import ru.i_novus.integration.model.PostResultModel;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
     *
     * @param messageCommonModel модель для запроса
     * @return возвращает Message c обьектом ответа
     */
    public Message prepareSyncRequest(Message<CommonModel> messageCommonModel) {
        return prepareRequest(messageCommonModel);
    }

    /**
     * Асинхронный запрос
     *
     * @param messageCommonModel модель для запроса
     */
    public void prepareAsyncRequest(Message<CommonModel> messageCommonModel) {
        prepareRequest(messageCommonModel);
    }

    /**
     * Подготовка и передача запроса получателю
     *
     * @param messageCommonModel модель для запроса
     * @return если вызов синхронный вернет Message c обьектом ответа иначе вернет SUCCESS
     */
    private Message prepareRequest(Message<CommonModel> messageCommonModel) {
        ParticipantModel participantModel = messageCommonModel.getPayload().getParticipantModel();

        Message message = null;
        Object result = null;
        ResponseEntity<Object> responseEntity;
        try {
            if (participantModel.getIntegrationType().equals("REST_GET")) {
                String url = participantModel.getUrl();
                responseEntity = restTemplate.getForEntity(new URI(url), Object.class);
                message = MessageBuilder.withPayload(responseEntity.getBody()).build();

                if (!participantModel.isSync())
                    checkError(responseEntity, messageCommonModel);

                result = responseEntity.getBody();

                if (messageCommonModel.getPayload().getMonitoringModel().getReceiver().equals("nsi")) {
                    List<String> list = Arrays.asList(url.split("&"));
                    String dataComment = list.stream().filter(l -> l.contains("identifier")).findFirst().get();
                    if (participantModel.getMethod().equals("data") && result.toString().contains("list=[]")) {

                        String versionComment = list.stream().filter(l -> l.contains("version")).findFirst().get();
                        monitoringNsiMessage(participantModel.getMethod(), messageCommonModel.getPayload().getMonitoringModel(),
                                MessageStatusEnum.SEND.getId(),
                                messageSource.getMessage("nsi.update.operation", null, Locale.ENGLISH) + dataComment + "-" + versionComment);
                    }
                    if (participantModel.getMethod().equals("versions")) {

                        monitoringNsiMessage(participantModel.getMethod(), messageCommonModel.getPayload().getMonitoringModel(),
                                MessageStatusEnum.SEND.getId(),
                                messageSource.getMessage("nsi.update.operation.version", null, Locale.ENGLISH) + dataComment);
                    }
                }

                return message;
            }
            if (participantModel.getIntegrationType().equals("REST_POST")) {
                try {
                    responseEntity = restTemplate.postForEntity(participantModel.getUrl(),
                            messageCommonModel.getPayload().getObject(), Object.class);
                } catch (RestClientResponseException e) {
                    if (!participantModel.isSync()) {
                        monitoringGateway.createError(MessageBuilder.withPayload(messageCommonModel.getPayload().getMonitoringModel()).build());
                        throw new RuntimeException(e.getResponseBodyAsString());
                    } else {
                        responseEntity = new ResponseEntity<>(e.getResponseBodyAsByteArray(), HttpStatus.valueOf(e.getRawStatusCode()));
                    }
                }

                result = responseEntity.getBody();
                if (participantModel.isSync() && result != null) {
                    PostResultModel postResultModel = new PostResultModel();
                    postResultModel.setRegion(participantModel.getReceiver());
                    postResultModel.setStatus(responseEntity.getStatusCode().toString());
                    postResultModel.setPayload(result);
                    message = MessageBuilder.withPayload(postResultModel).build();
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
            monitoringRequestErrorMessage(participantModel.getMethod(), messageCommonModel.getPayload().getMonitoringModel(),
                    MessageStatusEnum.ERROR.getId(), ex.getMessage(), StringUtils.left(String.valueOf(messageCommonModel.getPayload().getObject()), 1000));
            monitoringGateway.createError(MessageBuilder.withPayload(messageCommonModel.getPayload().getMonitoringModel()).build());
            if (participantModel.getIntegrationType().equals("REST_POST")) {
                if (participantModel.isSync()) {
                    PostResultModel postResultModel = new PostResultModel();
                    postResultModel.setRegion(participantModel.getReceiver());
                    postResultModel.setStatus("500");
                    postResultModel.setPayload(ex);
                    message = MessageBuilder.withPayload(postResultModel).build();
                } else {
                    message = MessageBuilder.withPayload(ex).build();
                }
                return message;
            } else {
                throw new RuntimeException(participantModel.getUrl(), ex);
            }
        }
        monitoringRequestMessage(participantModel.getMethod(), messageCommonModel.getPayload().getMonitoringModel(), MessageStatusEnum.SEND.getId());
        monitoringGateway.putToQueue(MessageBuilder.withPayload(messageCommonModel.getPayload().getMonitoringModel()).build());
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
     *
     * @param url      callBackUrl
     * @param response Object для передачи
     */
    private void sendCallBack(String url, Object response) {
        restTemplate.postForLocation(url, response);
    }

    /**
     * Заполнение мониторинга для NSI
     *
     * @param operation       выполненная операция
     * @param monitoringModel модель
     * @param status          статус передачи
     */
    private void monitoringNsiMessage(String operation, MonitoringModel monitoringModel, String status, String comment) {
        monitoringRequestMessage(operation, monitoringModel, status);
        monitoringModel.setComment(comment);
        monitoringGateway.putToQueue(MessageBuilder.withPayload(monitoringModel).build());
    }

    /**
     * Заполнение мониторинга для отправки
     *
     * @param operation       выполненная операция
     * @param monitoringModel модель
     * @param status          статус передачи
     */
    private void monitoringRequestMessage(String operation, MonitoringModel monitoringModel, String status) {
        monitoringModel.setDateTime(LocalDateTime.now());
        monitoringModel.setStatus(status);
        monitoringModel.setOperation(operation);
    }

    private void monitoringRequestErrorMessage(String operation, MonitoringModel monitoringModel, String status, String error, String comment) {
        monitoringModel.setError(error);
        monitoringRequestMessage(operation, monitoringModel, status);
        monitoringModel.setComment(comment);
    }
}
