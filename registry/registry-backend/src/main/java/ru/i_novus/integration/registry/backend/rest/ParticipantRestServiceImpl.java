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

@Controller
public class ParticipantRestServiceImpl implements ParticipantRestService {

    @Autowired
    private ParticipantRepository repository;

    @Autowired
    private RegistryAuditClient auditClient;

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
    public Participant getById(String id) {
        return map(repository.findById(id).orElse(null));
    }

    @Override
    public Participant create(Participant participant) {
        ParticipantEntity result = repository.save(map(participant));
        return audit("audit.participant.create" ,result);
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
        return audit("audit.participant.update", repository.save(entity));
    }

    @Override
    public void delete(String code) {
        audit("audit.participant.delete", repository.findById(code).orElse(null));
        repository.deleteById(code);
    }

    private Participant map(ParticipantEntity source) {
        if (source == null)
            return null;
        Participant target = new Participant();
        target.setCode(source.getCode());
        target.setDisable(source.getDisable());
        target.setGroupCode(source.getGroupCode());
        target.setName(source.getName());
        return target;
    }

    private ParticipantEntity map(Participant source) {
        if (source == null)
            return null;
        ParticipantEntity target = new ParticipantEntity();
        target.setCode(source.getCode());
        target.setDisable(source.getDisable());
        target.setGroupCode(source.getGroupCode());
        target.setName(source.getName());
        return target;
    }

    private Participant audit(String action, ParticipantEntity entity) {
        if (entity != null) {
            auditClient.audit(action, entity, "" + entity.getCode(), entity.getName());
        }
        return map(entity);
    }

    public void setRepository(ParticipantRepository repository) {
        this.repository = repository;
    }
}
