package ru.i_novus.integration.registry.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.i_novus.integration.registry.backend.entity.ParticipantEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantMethodEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity;
import ru.i_novus.integration.registry.backend.repository.ParticipantMethodRepository;
import ru.i_novus.integration.registry.backend.repository.ParticipantPermissionRepository;
import ru.i_novus.integration.registry.backend.repository.ParticipantRepository;
import ru.i_novus.integration.common.api.ParticipantModel;
import ru.i_novus.integration.common.api.RegistryInfoModel;

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

        if (!sender.isPresent()) throw new RuntimeException("service :" + model.getSender() + " disable");
        if (!receiver.isPresent()) throw new RuntimeException("service :" + model.getReceiver() + " disable");

        Optional<ParticipantMethodEntity> senderMethod = participantMethodRepository.find(model.getReceiver(), model.getMethod());

        List<ParticipantPermissionEntity> permissions;
        if (senderMethod.isPresent()) {
            permissions = participantPermissionRepository.find(senderMethod.get().getId(),
                    sender.get().getCode(), sender.get().getGroupCode());
        } else {
            throw new RuntimeException("method :" + model.getMethod() + "is not present to : " + model.getReceiver());
        }

        Optional<ParticipantPermissionEntity> permission = permissions.isEmpty() ? Optional.empty() : permissions.stream()
                .filter(p -> p.getParticipantCode() != null).findFirst();


        ParticipantModel participantModel = new ParticipantModel();
        if (permission.isPresent()) {
            participantModel.setUrl(senderMethod.get().getUrl());
            participantModel.setCallbackUrl(permission.get().getCallBackUrl());
            participantModel.setSync(permission.get().getSync());
            participantModel.setIntegrationType(senderMethod.get().getIntegrationType());
            participantModel.setMethod(senderMethod.get().getMethodCode());
        } else {
            throw new RuntimeException("permission error");
        }

        return participantModel;
    }
}
