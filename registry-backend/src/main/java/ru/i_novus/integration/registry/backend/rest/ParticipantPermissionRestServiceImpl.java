package ru.i_novus.integration.registry.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import ru.i_novus.integration.registry.api.rest.ParticipantPermissionRestService;
import ru.i_novus.integration.registry.api.criteria.ParticipantPermissionCriteria;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity;
import ru.i_novus.integration.registry.api.model.ParticipantPermission;
import ru.i_novus.integration.registry.backend.repository.ParticipantPermissionRepository;
import ru.i_novus.integration.registry.backend.specifications.ParticipantPermissionSpecifications;

@Controller
public class ParticipantPermissionRestServiceImpl implements ParticipantPermissionRestService, Mappers {

    private final ParticipantPermissionRepository repository;

    @Autowired
    public ParticipantPermissionRestServiceImpl(ParticipantPermissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ParticipantPermission> findAll(ParticipantPermissionCriteria criteria) {
        Specification<ParticipantPermissionEntity> specification = new ParticipantPermissionSpecifications(criteria);

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
        return map(result);
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
        return map(repository.save(entity));
    }

    @Override
    public void delete(Integer code) {
        repository.findById(code).ifPresent(this::map);
        repository.deleteById(code);
    }
}
