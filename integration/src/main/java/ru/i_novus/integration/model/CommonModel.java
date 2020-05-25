package ru.i_novus.integration.model;

import lombok.Getter;
import lombok.Setter;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.common.api.model.ParticipantModel;

import java.io.Serializable;
import java.util.StringJoiner;

@Getter
@Setter
public class CommonModel implements Serializable {
    private static final long serialVersionUID = -6482737092304911196L;

    private MonitoringModel monitoringModel;
    private ParticipantModel participantModel;
    private Object object;

    @Override
    public String toString() {
        return new StringJoiner(", ", CommonModel.class.getSimpleName() + "[", "]")
                .add("monitoringModel=" + monitoringModel)
                .add("participantModel=" + participantModel)
                .add("object=" + object)
                .toString();
    }
}
