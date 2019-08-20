package ru.i_novus.integration.model;

import lombok.Getter;
import lombok.Setter;
import ru.i_novus.is.integration.common.api.MonitoringModel;
import ru.i_novus.is.integration.common.api.ParticipantModel;

import java.io.Serializable;

@Getter
@Setter
public class CommonModel implements Serializable {
    private static final long serialVersionUID = -6482737092304911196L;

    private MonitoringModel monitoringModel;
    private ParticipantModel participantModel;
    private Object object;
}
