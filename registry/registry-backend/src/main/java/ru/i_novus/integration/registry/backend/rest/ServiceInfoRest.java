package ru.i_novus.integration.registry.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.registry.backend.entity.ParticipantEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantMethodEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity;
import ru.i_novus.integration.registry.backend.repository.ParticipantMethodRepository;
import ru.i_novus.integration.registry.backend.repository.ParticipantPermissionRepository;
import ru.i_novus.integration.registry.backend.repository.ParticipantRepository;
import ru.i_novus.is.integration.common.api.ParticipantModel;
import ru.i_novus.is.integration.common.api.RegistryInfoModel;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/registry/service")
public class ServiceInfoRest {

    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    ParticipantMethodRepository participantMethodRepository;
    @Autowired
    ParticipantPermissionRepository participantPermissionRepository;

    @PostMapping("/prepareRequest")
    public ParticipantModel getServiceInfo(@RequestBody RegistryInfoModel model) {
        Optional<ParticipantEntity> sender = participantRepository.findById(model.getSender());
        Optional<ParticipantEntity> receiver = participantRepository.findById(model.getReceiver());

        Optional<ParticipantMethodEntity> senderMethod = participantMethodRepository.find(model.getReceiver(), model.getMethod());

        List<ParticipantPermissionEntity> permissions = participantPermissionRepository.find(senderMethod.get().getId(),
                sender.get().getCode(), sender.get().getGroupCode());

        ParticipantPermissionEntity permission = permissions.isEmpty() ? null : permissions.stream()
                .filter(p-> p.getParticipantCode() != null).findFirst().get();

        ParticipantModel participantModel = new ParticipantModel();
        if (permission != null) {
            participantModel.setUrl(senderMethod.get().getUrl());
            participantModel.setCallbackUrl(permission.getCallbackUrl());
            participantModel.setSync(permission.isSync());
            participantModel.setIntegrationType(participantModel.getIntegrationType());
            participantModel.setMethod(senderMethod.get().getMethodCode());
        } else {
            throw new RuntimeException("permission error");
        }

        return participantModel;
    }
}