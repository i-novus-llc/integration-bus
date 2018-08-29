package ru.i_novus.integration.model;

public enum MessageStatusEnum {
    CREATE(1),
    ERROR(3),
    QUEUE(3),
    SEND(4);

    private int id;

    MessageStatusEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
