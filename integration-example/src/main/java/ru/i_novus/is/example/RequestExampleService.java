package ru.i_novus.is.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RequestExampleService {
    private final RestTemplate restTemplate;

    @Autowired
    public RequestExampleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<ResponseModel> sendGetRequest() {
        return restTemplate.getForEntity("http://localhost:8080/integration/get/main/getrequest/somePath?bar=bar", ResponseModel.class);
    }

    public void sendPostRequest() {
        RequestModel requestModel = new RequestModel();
        requestModel.setId(1);
        requestModel.setMessage("foo");
        restTemplate.postForLocation("http://localhost:8080/integration/get/main/postrequest", requestModel);
    }
}
