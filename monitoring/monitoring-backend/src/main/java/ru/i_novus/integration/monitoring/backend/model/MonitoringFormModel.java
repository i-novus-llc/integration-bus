package ru.i_novus.integration.monitoring.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringFormModel {
    private String uid;
    private String sender;
    private String receiver;
    private String operation;
}
