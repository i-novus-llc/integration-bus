package ru.i_novus.integration.registry.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import ru.i_novus.integration.registry.backend.api.ParticipantPermissionRestService;
import ru.i_novus.integration.registry.backend.audit.RegistryAuditClient;
import ru.i_novus.integration.registry.backend.criteria.ParticipantPermissionCriteria;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity;
import ru.i_novus.integration.registry.backend.model.ParticipantPermission;
import ru.i_novus.integration.registry.backend.repository.ParticipantPermissionRepository;
import ru.i_novus.integration.registry.backend.specifications.ParticipantPermissionSpecifications;

import java.util.Arrays;

@Controller
public class ParticipantPermissionRestServiceImpl implements ParticipantPermissionRestService, Mappers {

    @Autowired
    private ParticipantPermissionRepository repository;

    @Autowired
    private RegistryAuditClient auditClient;

    @Override
    public Page<ParticipantPermission> findAll(ParticipantPermissionCriteria criteria) {
        Specification<ParticipantPermissionEntity> specification = new ParticipantPermissionSpecifications(criteria);
        if (criteria.getOrders() == null) {
            criteria.setOrders(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "id")));
        } else {
            criteria.getOrders().add(new Sort.Order(Sort.Direction.ASC, "id"));
        }
        Page<ParticipantPermissionEntity> participantPermissions = repository.findAll(specification, criteria);
        return participantPermissions.map(this::map);
    }

    @Override
    public ParticipantPermission getById(Integer id) {
        return map(repository.findById(id).orElse(null));
    }

    @Override
    public ParticipantPermission create(ParticipantPermission participant) {
        ParticipantPermissionEntity result = repository.save(map(participant));
        return audit("audit.eventType.create", result);
    }

    @Override
    public ParticipantPermission update(ParticipantPermission participantPermission) {
        ParticipantPermissionEntity entity = repository.findById(participantPermission.getId()).orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException("Can't find participantPermission by id " + participantPermission.getId());
        }
        entity.setGroupCode(participantPermission.getGroupCode());
        entity.setCallBackUrl(participantPermission.getCallbackUrl());
        entity.setParticipantCode(participantPermission.getParticipantCode());
        entity.setParticipantMethodId(participantPermission.getParticipantMethodId());
        entity.setSync(participantPermission.isSync());
        return audit("audit.eventType.update", repository.save(entity));
    }

    @Override
    public void delete(Integer code) {
        repository.findById(code).ifPresent(ent -> audit("audit.eventType.delete", ent));
        repository.deleteById(code);
    }

    private ParticipantPermission audit(String action, ParticipantPermissionEntity entity) {
        if (entity != null) {
            auditClient.audit(action, entity, "" + entity.getId(), "audit.objectName.participantPermission");
        }
        return map(entity);
    }

    public void setRepository(ParticipantPermissionRepository repository) {
        this.repository = repository;
    }
}