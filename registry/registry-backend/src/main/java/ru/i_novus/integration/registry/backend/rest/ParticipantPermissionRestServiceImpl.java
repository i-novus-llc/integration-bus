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
public class ParticipantPermissionRestServiceImpl implements ParticipantPermissionRestService {

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
        return audit("audit.participantPermission.create", result);
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
        return audit("audit.participantPermission.update", repository.save(entity));
    }

    @Override
    public void delete(Integer code) {
        audit("audit.participantPermission.delete", repository.getOne(code));
        repository.deleteById(code);
    }

    private ParticipantPermission map(ParticipantPermissionEntity source) {
        if (source == null)
            return null;
        ParticipantPermission target = new ParticipantPermission();
        target.setId(source.getId());
        target.setGroupCode(source.getGroupCode());
        target.setCallbackUrl(source.getCallBackUrl());
        target.setParticipantCode(source.getParticipantCode());
        target.setParticipantMethodId(source.getParticipantMethodId());
        target.setSync(source.getSync());
        return target;
    }

    private ParticipantPermissionEntity map(ParticipantPermission source) {
        if (source == null)
            return null;
        ParticipantPermissionEntity target = new ParticipantPermissionEntity();
        if (source.getId() != null)
            target.setId(source.getId());
        target.setId(source.getId());
        target.setGroupCode(source.getGroupCode());
        target.setCallBackUrl(source.getCallbackUrl());
        target.setParticipantCode(source.getParticipantCode());
        target.setParticipantMethodId(source.getParticipantMethodId());
        target.setSync(source.isSync());
        return target;
    }

    private ParticipantPermission audit(String action, ParticipantPermissionEntity entity) {
        if (entity != null) {
            auditClient.audit(action, entity, "" + entity.getId(), entity.getParticipantCode());
        }
        return map(entity);
    }

    public void setRepository(ParticipantPermissionRepository repository) {
        this.repository = repository;
    }
}
