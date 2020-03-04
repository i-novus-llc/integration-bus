package ru.i_novus.integration.common.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantModel implements Serializable {
    private static final long serialVersionUID = -7352894916098242807L;

    private String url;
    private String callbackUrl;
    private boolean sync;
    private String integrationType;
    private String method;
    private String receiver;
}
