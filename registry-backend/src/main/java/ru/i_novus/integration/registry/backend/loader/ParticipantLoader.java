package ru.i_novus.integration.registry.backend.loader;

import net.n2oapp.platform.loader.server.ServerLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.i_novus.integration.registry.api.model.Participant;
import ru.i_novus.integration.registry.backend.repository.ParticipantRepository;
import ru.i_novus.integration.registry.backend.rest.Mappers;

import java.util.List;

/**
 * Загрузчик систем
 */

@Component
@Transactional
public class ParticipantLoader implements ServerLoader<Participant>, Mappers {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantLoader(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public void load(List<Participant> list, String s) {
        for (Participant participant : list) {
            participantRepository.save(map(participant));
        }
    }

    @Override
    public String getTarget() {
        return "integration_participant";
    }

    @Override
    public Class<Participant> getDataType() {
        return Participant.class;
    }
}
