package ru.i_novus.is.example;

public class ResponseModel {
    private String message;

    public ResponseModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
