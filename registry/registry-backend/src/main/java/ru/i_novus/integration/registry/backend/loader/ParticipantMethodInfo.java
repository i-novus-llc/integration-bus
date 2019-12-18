package ru.i_novus.integration.registry.backend.loader;

import lombok.Getter;
import lombok.Setter;
import ru.i_novus.integration.registry.backend.model.ParticipantMethod;
import ru.i_novus.integration.registry.backend.model.ParticipantPermission;

import java.util.List;

@Getter
@Setter
public class ParticipantMethodInfo extends ParticipantMethod {
    private List<ParticipantPermission> permissions;
}
