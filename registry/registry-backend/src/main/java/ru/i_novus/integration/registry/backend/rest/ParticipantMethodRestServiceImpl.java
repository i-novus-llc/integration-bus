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
import ru.i_novus.integration.registry.backend.model.IntegrationType;
import ru.i_novus.integration.registry.backend.model.ParticipantMethod;
import ru.i_novus.integration.registry.backend.repository.IntegrationTypeRepository;
import ru.i_novus.integration.registry.backend.repository.ParticipantMethodRepository;
import ru.i_novus.integration.registry.backend.specifications.ParticipantMethodSpecifications;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ParticipantMethodRestServiceImpl implements ParticipantMethodRestService, Mappers {

    @Autowired
    private ParticipantMethodRepository repository;

    @Autowired
    private RegistryAuditClient auditClient;

    @Autowired
    private IntegrationTypeRepository integrationTypeRepository;

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
        return audit("audit.eventType.create", result);
    }

    @Override
    public ParticipantMethod update(ParticipantMethod participantMethod) {
        ParticipantMethodEntity entity = repository.findById(participantMethod.getId()).orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException("Can't find participantMethod by id " + participantMethod.getId());
        }
        entity.setDisable(participantMethod.getDisable());
        entity.setIntegrationType(map(participantMethod.getIntegrationType()));
        entity.setMethodCode(participantMethod.getMethodCode());
        entity.setUrl(participantMethod.getUrl());
        return audit("audit.eventType.update", repository.save(entity));
    }

    @Override
    public void delete(Integer code) {
        repository.findById(code).ifPresent(ent -> audit("audit.eventType.delete", ent));
        repository.deleteById(code);
    }

    @Override
    public List<IntegrationType> getAllIntegrationTypes() {
        return integrationTypeRepository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    private ParticipantMethod audit(String action, ParticipantMethodEntity entity) {
        if (entity != null) {
            auditClient.audit(action, entity, "" + entity.getId(), "audit.objectName.participantMethod");
        }
        return map(entity);
    }

    public void setRepository(ParticipantMethodRepository repository) {
        this.repository = repository;
    }
}
