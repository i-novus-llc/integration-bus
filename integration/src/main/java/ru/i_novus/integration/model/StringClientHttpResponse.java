package ru.i_novus.integration.model;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.web.client.RestClientResponseException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StringClientHttpResponse extends AbstractClientHttpResponse {

    private int status;

    private byte[] body;

    private HttpHeaders headers;

    public StringClientHttpResponse(int status, String body, HttpHeaders headers) {
        this.status = status;
        this.body = body.getBytes();
        this.headers = headers;
    }

    public StringClientHttpResponse(RestClientResponseException e) {
        this.status = e.getRawStatusCode();
        this.body = e.getResponseBodyAsByteArray();
        this.headers = e.getResponseHeaders();
    }

    @Override
    public int getRawStatusCode() {
        return status;
    }

    @Override
    public String getStatusText() throws IOException {
        return getStatusCode().getReasonPhrase();
    }

    @Override
    public void close() {

    }

    @Override
    public InputStream getBody() {
        return new ByteArrayInputStream(body);
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
