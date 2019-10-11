package ru.i_novus.integration.registry.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import ru.i_novus.integration.registry.backend.api.ParticipantMethodRestService;
import ru.i_novus.integration.registry.backend.audit.RegistryAuditClient;
import ru.i_novus.integration.registry.backend.criteria.ParticipantMethodCriteria;
import ru.i_novus.integration.registry.backend.entity.ParticipantMethodEntity;
import ru.i_novus.integration.registry.backend.model.ParticipantMethod;
import ru.i_novus.integration.registry.backend.repository.ParticipantMethodRepository;
import ru.i_novus.integration.registry.backend.specifications.ParticipantMethodSpecifications;

import java.util.Arrays;

@Controller
public class ParticipantMethodRestServiceImpl implements ParticipantMethodRestService {

    @Autowired
    private ParticipantMethodRepository repository;

    @Autowired
    private RegistryAuditClient auditClient;

    @Override
    public Page<ParticipantMethod> findAll(ParticipantMethodCriteria criteria) {
        Specification<ParticipantMethodEntity> specification = new ParticipantMethodSpecifications(criteria);
        if (criteria.getOrders() == null) {
            criteria.setOrders(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "id")));
        } else {
            criteria.getOrders().add(new Sort.Order(Sort.Direction.ASC, "id"));
        }
        Page<ParticipantMethodEntity> participants = repository.findAll(specification, criteria);
        return participants.map(this::map);
    }

    @Override
    public ParticipantMethod getById(Integer id) {
        return map(repository.findById(id).orElse(null));
    }

    @Override
    public ParticipantMethod create(ParticipantMethod participant) {
        ParticipantMethodEntity result = repository.save(map(participant));
        return audit("audit.participantMethod.create", result);
    }

    @Override
    public ParticipantMethod update(ParticipantMethod participantMethod) {
        ParticipantMethodEntity entity = repository.findById(participantMethod.getId()).orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException("Can't find participantMethod by id " + participantMethod.getId());
        }
        entity.setDisable(participantMethod.getDisable());
        entity.setIntegrationType(participantMethod.getIntegrationType());
        entity.setMethodCode(participantMethod.getMethodCode());
        entity.setUrl(participantMethod.getUrl());
        return audit("audit.participantMethod.update", repository.save(entity));
    }

    @Override
    public void delete(Integer code) {
        audit("audit.participantMethod.delete", repository.getOne(code));
        repository.deleteById(code);
    }

    private ParticipantMethod map(ParticipantMethodEntity source) {
        if (source == null)
            return null;
        ParticipantMethod target = new ParticipantMethod();
        target.setDisable(source.getDisable());
        target.setId(source.getId());
        target.setIntegrationType(source.getIntegrationType());
        target.setMethodCode(source.getMethodCode());
        target.setParticipantCode(source.getParticipantCode());
        target.setUrl(source.getUrl());
        return target;
    }

    private ParticipantMethodEntity map(ParticipantMethod source) {
        if (source == null)
            return null;
        ParticipantMethodEntity target = new ParticipantMethodEntity();
        target.setDisable(source.getDisable());
        if (source.getId() != null)
            target.setId(source.getId());
        target.setIntegrationType(source.getIntegrationType());
        target.setMethodCode(source.getMethodCode());
        target.setParticipantCode(source.getParticipantCode());
        target.setUrl(source.getUrl());
        return target;
    }

    private ParticipantMethod audit(String action, ParticipantMethodEntity entity) {
        if (entity != null) {
            auditClient.audit(action, entity, "" + entity.getId(), entity.getParticipantCode());
        }
        return map(entity);
    }

    public void setRepository(ParticipantMethodRepository repository) {
        this.repository = repository;
    }
}
