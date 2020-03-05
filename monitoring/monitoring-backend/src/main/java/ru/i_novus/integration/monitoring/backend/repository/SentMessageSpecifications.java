package ru.i_novus.integration.monitoring.backend.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.i_novus.integration.monitoring.backend.criteria.SentMessageCriteria;
import ru.i_novus.integration.monitoring.backend.entity.SentMessageEntity;
import ru.i_novus.integration.monitoring.backend.entity.SentMessageEntity_;

public class SentMessageSpecifications {

    public static Specification<SentMessageEntity> equalCriteriaParams(SentMessageCriteria criteria) {
        Specification<SentMessageEntity> spec = Specification.where(null);
        if (criteria.getUid() != null) {
            spec = spec.and(equalUid(criteria));
        }
        if (criteria.getSender() != null) {
            spec = spec.and(equalSender(criteria));
        }
        if (criteria.getReceiver() != null) {
            spec = spec.and(equalReceiver(criteria));
        }
        if (criteria.getSentDateTime() != null) {
            spec = spec.and(greaterThanDateFromOrEqual(criteria));
        }
        if (criteria.getDateTo() != null) {
            spec = spec.and(lessThanDateToOrEqual(criteria));
        }
        if (criteria.getStatus() != null) {
            spec = spec.and(equalStatus(criteria));
        }

        if (criteria.getComment() != null) {
            spec = spec.and(equalComment(criteria));
        }
        return spec;
    }

    private static Specification<SentMessageEntity> equalUid(SentMessageCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(SentMessageEntity_.uid), criteria.getUid());
    }

    private static Specification<SentMessageEntity> equalSender(SentMessageCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(SentMessageEntity_.sender), criteria.getSender());
    }

    private static Specification<SentMessageEntity> equalReceiver(SentMessageCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(SentMessageEntity_.receiver), criteria.getReceiver());
    }

    private static Specification<SentMessageEntity> lessThanDateToOrEqual(SentMessageCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                root.get(SentMessageEntity_.sentDateTime), criteria.getDateTo());
    }

    private static Specification<SentMessageEntity> greaterThanDateFromOrEqual(SentMessageCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                root.get(SentMessageEntity_.sentDateTime), criteria.getSentDateTime());
    }

    private static Specification<SentMessageEntity> equalStatus(SentMessageCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(SentMessageEntity_.currentStatus), criteria.getStatus());
    }

    private static Specification<SentMessageEntity> equalComment(SentMessageCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(SentMessageEntity_.comment), criteria.getComment());
    }
}
