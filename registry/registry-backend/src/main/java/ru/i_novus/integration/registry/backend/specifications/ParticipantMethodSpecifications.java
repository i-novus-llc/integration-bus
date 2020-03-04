package ru.i_novus.integration.registry.backend.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.i_novus.integration.registry.backend.criteria.ParticipantMethodCriteria;
import ru.i_novus.integration.registry.backend.entity.ParticipantMethodEntity;
import ru.i_novus.integration.registry.backend.entity.ParticipantMethodEntity_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ParticipantMethodSpecifications implements Specification<ParticipantMethodEntity> {
    private static final long serialVersionUID = -4719617411733919264L;

    private ParticipantMethodCriteria criteria;

    public ParticipantMethodSpecifications(ParticipantMethodCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ParticipantMethodEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        Predicate predicate = builder.and();
        if (criteria.getParticipantCode() != null)
            predicate = builder.and(predicate, builder.equal(root.get(ParticipantMethodEntity_.participantCode), criteria.getParticipantCode()));
        if (criteria.getMethodCode() != null)
            predicate = builder.and(predicate, builder.equal(root.get(ParticipantMethodEntity_.methodCode), criteria.getMethodCode()));
        return predicate;
    }
}