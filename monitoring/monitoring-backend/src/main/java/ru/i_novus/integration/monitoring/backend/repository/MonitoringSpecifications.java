package ru.i_novus.integration.monitoring.backend.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.i_novus.integration.monitoring.backend.MonitoringCriteria;
import ru.i_novus.integration.monitoring.backend.entity.MonitoringEntity;
import ru.i_novus.integration.monitoring.backend.entity.MonitoringEntity_;

public class MonitoringSpecifications {

    public static Specification<MonitoringEntity> equalCriteriaParams(MonitoringCriteria criteria) {
        Specification<MonitoringEntity> spec = Specification.where(null);
        if (criteria.getUid() != null) {
            spec = spec.and(equalUid(criteria));
        }
        if (criteria.getSender() != null) {
            spec = spec.and(equalSender(criteria));
        }
        if (criteria.getReceiver() != null) {
            spec = spec.and(equalReceiver(criteria));
        }
        if (criteria.getDateFrom() != null) {
            spec = spec.and(greaterThanDateFromOrEqual(criteria));
        }
        if (criteria.getDateTo() != null) {
            spec = spec.and(lessThanDateToOrEqual(criteria));
        }
        if (criteria.getComment() != null) {
            spec = spec.and(equalComment(criteria));
        }
        if (criteria.getStatus() != null) {
            spec = spec.and(equalStatus(criteria));
        }
        return spec;
    }

    private static Specification<MonitoringEntity> equalUid(MonitoringCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(MonitoringEntity_.uid), criteria.getUid());
    }

    private static Specification<MonitoringEntity> equalSender(MonitoringCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(MonitoringEntity_.sender), criteria.getSender());
    }

    private static Specification<MonitoringEntity> equalReceiver(MonitoringCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(MonitoringEntity_.receiver), criteria.getReceiver());
    }

    private static Specification<MonitoringEntity> equalComment(MonitoringCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(MonitoringEntity_.comment), criteria.getComment());
    }

    private static Specification<MonitoringEntity> lessThanDateToOrEqual(MonitoringCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                root.get(MonitoringEntity_.dateTime), criteria.getDateTo());
    }

    private static Specification<MonitoringEntity> greaterThanDateFromOrEqual(MonitoringCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                root.get(MonitoringEntity_.dateTime), criteria.getDateFrom());
    }

    private static Specification<MonitoringEntity> equalStatus(MonitoringCriteria criteria) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(MonitoringEntity_.status), criteria.getStatus());
    }
}
