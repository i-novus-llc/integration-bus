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

import java.util.List;


/**
 * Загрузчик сервисов, пермишенов
 */

@Component
@Transactional
public class ParticipantMethodLoader implements ServerLoader<ParticipantMethodInfo>, Mappers {

    @Autowired
    private ParticipantMethodRepository participantMethodRepository;

    @Autowired
    private ParticipantPermissionRepository participantPermissionRepository;

    @Override
    public void load(List<ParticipantMethodInfo> list, String participantCode) {
        deleteOldMethods(list, participantCode);
        for (ParticipantMethodInfo method : list) {
            ParticipantMethodEntity methodEntity = participantMethodRepository.save(map(method));
            if (method.getPermissions()!= null) {
                for(ParticipantPermission permission : method.getPermissions()) {
                    ParticipantPermissionEntity permissionEntity = map(permission);
                    permissionEntity.setParticipantMethodId(methodEntity.getId());
                    participantPermissionRepository.save(permissionEntity);
                }
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
            deletePermissions(entity.getId());
            ParticipantMethodInfo method = list.stream().filter(m -> entity.getMethodCode().equals(m.getMethodCode())).findFirst().orElse(null);
            if (method == null) {
                participantMethodRepository.delete(entity);
            } else {
                method.setId(entity.getId());
            }
        }
    }

    private void deletePermissions(Integer methodId) {
        ParticipantPermissionCriteria criteria = new ParticipantPermissionCriteria();
        criteria.setParticipantMethodId(methodId);
        List<ParticipantPermissionEntity> forDelete = participantPermissionRepository.findAll(new ParticipantPermissionSpecifications(criteria));
        participantPermissionRepository.deleteAll(forDelete);
    }
}
