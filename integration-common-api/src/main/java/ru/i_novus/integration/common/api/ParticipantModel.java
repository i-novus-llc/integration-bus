package ru.i_novus.integration.common.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ParticipantModel implements Serializable {
    private static final long serialVersionUID = -3981184676777114798L;

    private String url;
    private String callbackUrl;
    private boolean sync;
    private String integrationType;
    private String method;
    private String receiver;
}
