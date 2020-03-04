package ru.i_novus.integration.model;

public enum MessageStatusEnum {
    CREATE("CREATE"),
    ERROR("ERROR"),
    QUEUE("QUEUE"),
    SEND("SEND");

    private String id;

    MessageStatusEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
