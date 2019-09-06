package ru.i_novus.integration.registry.backend.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.i_novus.integration.registry.backend.criteria.ParticipantCriteria;
import ru.i_novus.integration.registry.backend.entity.ParticipantEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantEntity_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
        return predicate;
    }
}