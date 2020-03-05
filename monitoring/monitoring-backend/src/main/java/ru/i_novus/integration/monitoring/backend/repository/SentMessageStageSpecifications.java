package ru.i_novus.integration.monitoring.backend.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.i_novus.integration.monitoring.backend.criteria.SentMessageStageCriteria;
import ru.i_novus.integration.monitoring.backend.entity.SentMessageStageEntity;
import ru.i_novus.integration.monitoring.backend.entity.SentMessageStageEntity_;

public class SentMessageStageSpecifications {

    public static Specification<SentMessageStageEntity> equalCriteriaParams(SentMessageStageCriteria criteria) {
        Specification<SentMessageStageEntity> spec = Specification.where(null);

        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(SentMessageStageEntity_.sentMessageId), criteria.getSentMessageId()));

        return spec;
    }
}
