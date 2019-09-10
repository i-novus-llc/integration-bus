package ru.i_novus.integration.registry.backend.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.i_novus.integration.registry.backend.criteria.ParticipantPermissionCriteria;
import ru.i_novus.integration.registry.backend.entity.ParticipantEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantEntity_;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity_;
import ru.i_novus.integration.registry.backend.model.ParticipantPermission;

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
        return predicate;
    }
}