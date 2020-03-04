package ru.i_novus.integration.registry.backend.loader;

import net.n2oapp.platform.loader.server.ServerLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.i_novus.integration.registry.backend.criteria.ParticipantMethodCriteria;
import ru.i_novus.integration.registry.backend.criteria.ParticipantPermissionCriteria;
import ru.i_novus.integration.registry.backend.entity.ParticipantMethodEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity;

import ru.i_novus.integration.registry.backend.model.ParticipantPermission;
import ru.i_novus.integration.registry.backend.repository.ParticipantMethodRepository;
import ru.i_novus.integration.registry.backend.repository.ParticipantPermissionRepository;
import ru.i_novus.integration.registry.backend.rest.Mappers;
import ru.i_novus.integration.registry.backend.specifications.ParticipantMethodSpecifications;
import ru.i_novus.integration.registry.backend.specifications.ParticipantPermissionSpecifications;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Загрузчик сервисов, пермишенов
 */

@Component
@Transactional
public class ParticipantMethodLoader implements ServerLoader<ParticipantMethodInfo>, Mappers {

    private final ParticipantMethodRepository participantMethodRepository;

    private final ParticipantPermissionRepository participantPermissionRepository;

    @Autowired
    public ParticipantMethodLoader(ParticipantMethodRepository participantMethodRepository, ParticipantPermissionRepository participantPermissionRepository) {
        this.participantMethodRepository = participantMethodRepository;
        this.participantPermissionRepository = participantPermissionRepository;
    }

    @Override
    public void load(List<ParticipantMethodInfo> list, String participantCode) {
        deleteOldMethods(list, participantCode);
        for (ParticipantMethodInfo method : list) {
            ParticipantMethodEntity methodEntity = participantMethodRepository.save(map(method));
            if (method.getPermissions() != null && !method.getPermissions().isEmpty()) {
                mergePermissions(methodEntity.getId(), method.getPermissions());
            } else {
                deletePermissions(methodEntity.getId());
            }
        }
    }

    @Override
    public String getTarget() {
        return "integration_method";
    }

    @Override
    public Class<ParticipantMethodInfo> getDataType() {
        return ParticipantMethodInfo.class;
    }

    private void deleteOldMethods(List<ParticipantMethodInfo> list, String participantCode) {
        ParticipantMethodCriteria criteria = new ParticipantMethodCriteria();
        criteria.setParticipantCode(participantCode);
        List<ParticipantMethodEntity> exists = participantMethodRepository.findAll(new ParticipantMethodSpecifications(criteria));
        for (ParticipantMethodEntity entity : exists) {
            ParticipantMethodInfo method = list.stream().filter(m -> entity.getMethodCode().equals(m.getMethodCode())).findFirst().orElse(null);
            if (method == null) {
                deletePermissions(entity.getId());
                participantMethodRepository.delete(entity);
            } else {
                method.setId(entity.getId());
            }
        }
    }

    private void mergePermissions(Integer methodId, List<ParticipantPermission> permissions) {
        ParticipantPermissionCriteria criteria = new ParticipantPermissionCriteria();
        criteria.setParticipantMethodId(methodId);
        List<ParticipantPermissionEntity> exists = participantPermissionRepository.findAll(new ParticipantPermissionSpecifications(criteria));
        Iterator<ParticipantPermissionEntity> iterEntity = exists.iterator();
        while (iterEntity.hasNext()) {
            ParticipantPermissionEntity entity = iterEntity.next();
            Iterator<ParticipantPermission> iterModel = permissions.iterator();
            while (iterModel.hasNext()) {
                ParticipantPermission permission = iterModel.next();
                if (isEqual(permission, entity)) {
                    iterEntity.remove();
                    iterModel.remove();
                    break;
                }
            }
        }

        if (!exists.isEmpty()) {
            participantPermissionRepository.deleteAll(exists);
        }

        if (!permissions.isEmpty()) {
            List<ParticipantPermissionEntity> toSave = permissions.stream().map(p -> {
                p.setParticipantMethodId(methodId);
                return map(p);
            }).collect(Collectors.toList());
            participantPermissionRepository.saveAll(toSave);
        }
    }

    //Игнорятся ParticipantMethodId и ID
    private boolean isEqual(ParticipantPermission p, ParticipantPermissionEntity e) {
        return Objects.equals(p.getParticipantCode(), e.getParticipantCode()) &&
                Objects.equals(p.getGroupCode(), e.getGroupCode()) &&
                Objects.equals(p.getCallbackUrl(), e.getCallBackUrl()) &&
                Objects.equals(p.isSync(), e.getSync());
    }

    private void deletePermissions(Integer methodId) {
        ParticipantPermissionCriteria criteria = new ParticipantPermissionCriteria();
        criteria.setParticipantMethodId(methodId);
        List<ParticipantPermissionEntity> forDelete = participantPermissionRepository.findAll(new ParticipantPermissionSpecifications(criteria));
        if (!forDelete.isEmpty()) {
            participantPermissionRepository.deleteAll(forDelete);
        }
    }
}
