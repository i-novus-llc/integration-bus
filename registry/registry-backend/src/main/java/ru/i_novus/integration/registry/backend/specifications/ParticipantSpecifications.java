package ru.i_novus.integration.registry.backend.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.i_novus.integration.registry.backend.criteria.ParticipantCriteria;
import ru.i_novus.integration.registry.backend.entity.*;
import ru.i_novus.integration.registry.backend.model.ParticipantMethod;

import javax.persistence.criteria.*;

public class ParticipantSpecifications implements Specification<ParticipantEntity> {
    private ParticipantCriteria criteria;

    public ParticipantSpecifications(ParticipantCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ParticipantEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        Predicate predicate = builder.and();
        if (criteria.getCode() != null)
            predicate = builder.and(predicate, builder.like(builder.lower(root.get(ParticipantEntity_.code)), criteria.getCode().toLowerCase() + "%"));
        if (criteria.getName() != null)
            predicate = builder.and(predicate, builder.like(builder.lower(root.get(ParticipantEntity_.name)), criteria.getName().toLowerCase() + "%"));
        if (criteria.getDisableSelectId() != null) {
            if (criteria.getDisableSelectId().equals(1)) {
                predicate = builder.and(predicate, builder.equal(root.get(ParticipantEntity_.disable), true));
            } else {
                predicate = builder.and(predicate, builder.equal(root.get(ParticipantEntity_.disable), false));
            }
        }
        if (criteria.getExcludeParticipantMethodId() != null) {
            Subquery<String> sq = criteriaQuery.subquery(String.class);
            Root<ParticipantMethodEntity> participantEntityRoot  = sq.from(ParticipantMethodEntity.class);
            sq.select(participantEntityRoot.get(ParticipantMethodEntity_.participantCode)).where(
                    builder.equal(participantEntityRoot.get(ParticipantMethodEntity_.ID),
                            criteria.getExcludeParticipantMethodId()));
            predicate = builder.and(predicate, builder.notEqual(root.get(ParticipantEntity_.code), sq));
        }
        return predicate;
    }
}