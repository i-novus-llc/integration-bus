package ru.i_novus.integration.monitoring.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentMessageModel {
    private Integer id;
    private String uid;
    private LocalDateTime sentDateTime;
    private String sender;
    private String receiver;
    private String operation;
    private String comment;
    private String status;
}
