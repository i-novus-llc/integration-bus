package ru.i_novus.integration.monitoring.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentMessageStageModel {
    private Integer id;
    private LocalDateTime dateTime;
    private String status;
    private String error;
    private String comment;
}
