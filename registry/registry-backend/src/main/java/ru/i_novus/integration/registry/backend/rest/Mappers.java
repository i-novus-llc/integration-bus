package ru.i_novus.integration.registry.backend.rest;

import ru.i_novus.integration.registry.backend.entity.*;
import ru.i_novus.integration.registry.backend.model.*;

/**
 * Мапперы для приведения Entity к Model и обратно
 */

public interface Mappers {

    // ParticipantRestServiceImpl

    default Participant map(ParticipantEntity source) {
        if (source == null)
            return null;
        Participant target = new Participant();
        target.setCode(source.getCode());
        target.setDisable(source.getDisable());
        target.setGroupCode(source.getGroupCode());
        target.setName(source.getName());
        return target;
    }

    default ParticipantEntity map(Participant source) {
        if (source == null)
            return null;
        ParticipantEntity target = new ParticipantEntity();
        target.setCode(source.getCode());
        target.setDisable(source.getDisable());
        target.setGroupCode(source.getGroupCode());
        target.setName(source.getName());
        return target;
    }

    // ParticipantMethodRestServiceImpl

    default ParticipantMethod map(ParticipantMethodEntity source) {
        if (source == null)
            return null;
        ParticipantMethod target = new ParticipantMethod();
        target.setDisable(source.getDisable());
        target.setId(source.getId());
        target.setIntegrationType(map(source.getIntegrationType()));
        target.setMethodCode(source.getMethodCode());
        target.setParticipantCode(source.getParticipantCode());
        target.setUrl(source.getUrl());

        return target;
    }

    default ParticipantMethodEntity map(ParticipantMethod source) {
        if (source == null)
            return null;
        ParticipantMethodEntity target = new ParticipantMethodEntity();
        target.setDisable(source.getDisable());
        if (source.getId() != null)
            target.setId(source.getId());
        target.setIntegrationType(map(source.getIntegrationType()));
        target.setMethodCode(source.getMethodCode());
        target.setParticipantCode(source.getParticipantCode());
        target.setUrl(source.getUrl());

        return target;
    }

    default IntegrationTypeEntity map(IntegrationType source) {
        return source == null ? null : new IntegrationTypeEntity(source.getId(), source.getName());
    }

    default IntegrationType map(IntegrationTypeEntity source) {
        return source == null ? null : new IntegrationType(source.getCode(), source.getName());
    }

    // ParticipantPermissionRestServiceImpl

    default ParticipantPermission map(ParticipantPermissionEntity source) {
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

    default ParticipantPermissionEntity map(ParticipantPermission source) {
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


}
