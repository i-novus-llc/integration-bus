package ru.i_novus.integration.registry.backend.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.i_novus.integration.common.api.model.ParticipantModel;
import ru.i_novus.integration.common.api.model.RegistryInfoModel;
import ru.i_novus.integration.registry.backend.api.AuthorizationService;
import ru.i_novus.integration.registry.backend.api.PrepareRequestService;
import ru.i_novus.integration.registry.backend.entity.ParticipantEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantMethodEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity;
import ru.i_novus.integration.registry.backend.repository.ParticipantMethodRepository;
import ru.i_novus.integration.registry.backend.repository.ParticipantPermissionRepository;
import ru.i_novus.integration.registry.backend.repository.ParticipantRepository;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Controller
public class ServiceInfoRest implements PrepareRequestService {

    private ParticipantRepository participantRepository;
    private ParticipantMethodRepository participantMethodRepository;
    private ParticipantPermissionRepository participantPermissionRepository;
    private AuthorizationService authorizationService;

    public ServiceInfoRest(ParticipantRepository participantRepository,
                           ParticipantMethodRepository participantMethodRepository,
                           ParticipantPermissionRepository participantPermissionRepository,
                           AuthorizationService authorizationService) {
        this.participantRepository = participantRepository;
        this.participantMethodRepository = participantMethodRepository;
        this.participantPermissionRepository = participantPermissionRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public ParticipantModel getServiceInfo(@RequestBody RegistryInfoModel model) {
        Optional<ParticipantEntity> sender = participantRepository.findEnabledById(model.getSender());
        Optional<ParticipantEntity> receiver = participantRepository.findEnabledById(model.getReceiver());

        if (!sender.isPresent()) throw new RuntimeException("service :" + model.getSender() + " disable");
        if (!receiver.isPresent()) throw new RuntimeException("service :" + model.getReceiver() + " disable");

        Optional<ParticipantMethodEntity> senderMethod = participantMethodRepository.findEnabled(model.getReceiver(),
                model.getMethod());

        List<ParticipantPermissionEntity> permissions;
        if (senderMethod.isPresent()) {
            permissions = participantPermissionRepository.find(senderMethod.get().getId(),
                    sender.get().getCode(), sender.get().getGroupCode());
        } else {
            throw new RuntimeException("method :" + model.getMethod() + "is not present to : " + model.getReceiver());
        }

        Optional<ParticipantPermissionEntity> permission = permissions.isEmpty() ? Optional.empty() :
                permissions.stream()
                        .filter(p -> p.getParticipantCode() != null).findFirst();


        ParticipantModel participantModel = new ParticipantModel();
        if (permission.isPresent()) {
            participantModel.setUrl(senderMethod.get().getUrl());
            participantModel.setCallbackUrl(permission.get().getCallBackUrl());
            participantModel.setSync(permission.get().getSync());
            participantModel.setIntegrationType(senderMethod.get().getIntegrationType().getCode());
            participantModel.setMethod(senderMethod.get().getMethodCode());
        } else {
            throw new RuntimeException("permission" + model.getSender() + "to" + model.getReceiver() + "not present");
        }

        return participantModel;
    }

    @Override
    public Response checkAuthorization(RegistryInfoModel model) {
        Optional<ParticipantEntity> opt = participantRepository.findById(model.getReceiver());
        if (opt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity(model.getReceiver() + " not found").build();
        }
        ParticipantEntity participant = opt.get();
        if (participant.getHasAuth() != null && participant.getHasAuth()) {
            boolean isValid = authorizationService.isValidToken(model.getAuthToken());
            if (!isValid) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        }
        return Response.status(Response.Status.OK).build();
    }
}
