package ru.i_novus.integration.registry.backend.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.i_novus.integration.registry.backend.criteria.ParticipantPermissionCriteria;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity_;

import javax.persistence.criteria.*;

public class ParticipantPermissionSpecifications implements Specification<ParticipantPermissionEntity> {

    private ParticipantPermissionCriteria criteria;

    public ParticipantPermissionSpecifications(ParticipantPermissionCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ParticipantPermissionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        Predicate predicate = builder.and();
        if (criteria.getParticipantMethodId() != null)
            predicate = builder.and(predicate, builder.equal(root.get(ParticipantPermissionEntity_.participantMethodId),
                    criteria.getParticipantMethodId()));

        if (criteria.getParticipantCode() != null)
            predicate = builder.and(predicate, builder.equal(root.get(ParticipantPermissionEntity_.participantCode),
                    criteria.getParticipantCode()));
        return predicate;
    }
}