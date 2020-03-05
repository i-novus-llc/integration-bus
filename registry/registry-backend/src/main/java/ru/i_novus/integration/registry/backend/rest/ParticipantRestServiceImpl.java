package ru.i_novus.integration.registry.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import ru.i_novus.integration.registry.backend.api.ParticipantRestService;
import ru.i_novus.integration.registry.backend.audit.RegistryAuditClient;
import ru.i_novus.integration.registry.backend.criteria.ParticipantCriteria;
import ru.i_novus.integration.registry.backend.entity.ParticipantEntity;
import ru.i_novus.integration.registry.backend.model.Participant;
import ru.i_novus.integration.registry.backend.repository.ParticipantRepository;
import ru.i_novus.integration.registry.backend.specifications.ParticipantSpecifications;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ParticipantRestServiceImpl implements ParticipantRestService, Mappers {

    private final ParticipantRepository repository;

    private final RegistryAuditClient auditClient;

    @Autowired
    public ParticipantRestServiceImpl(ParticipantRepository repository, RegistryAuditClient auditClient) {
        this.repository = repository;
        this.auditClient = auditClient;
    }

    @Override
    public Page<Participant> findAll(ParticipantCriteria criteria) {
        Specification<ParticipantEntity> specification = new ParticipantSpecifications(criteria);
        if (criteria.getOrders() == null) {
            criteria.setOrders(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "code")));
        } else {
            criteria.getOrders().add(new Sort.Order(Sort.Direction.ASC, "code"));
        }
        Page<ParticipantEntity> participants = repository.findAll(specification, criteria);
        return participants.map(this::map);
    }

    @Override
    public List<Participant> list(ParticipantCriteria criteria) {
        List<ParticipantEntity> entities =  repository.findAll();

        return entities
                .stream()
                .map(e-> new Participant(e.getCode(), e.getGroupCode(), e.getName(), e.getDisable()))
                .collect(Collectors.toList());
    }

    @Override
    public Participant getById(String id) {
        return map(repository.findById(id).orElse(null));
    }

    @Override
    public Participant create(Participant participant) {
        ParticipantEntity result = repository.save(map(participant));
        return audit("audit.eventType.create" ,result);
    }

    @Override
    public Participant update(Participant participant) {
        ParticipantEntity entity = repository.findById(participant.getCode()).orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException("Can't find participant by code " + participant.getCode());
        }
        entity.setName(participant.getName());
        entity.setDisable(participant.getDisable());
        entity.setGroupCode(participant.getGroupCode());
        return audit("audit.eventType.update", repository.save(entity));
    }

    @Override
    public void delete(String code) {
        audit("audit.eventType.delete", repository.findById(code).orElse(null));
        repository.deleteById(code);
    }

    private Participant audit(String action, ParticipantEntity entity) {
        if (entity != null) {
            auditClient.audit(action, entity, entity.getCode(), "audit.objectName.participant");
        }
        return map(entity);
    }
}
