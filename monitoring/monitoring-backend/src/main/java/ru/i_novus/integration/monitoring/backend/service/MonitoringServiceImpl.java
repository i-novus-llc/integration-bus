package ru.i_novus.integration.monitoring.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.api.criteria.SentMessageCriteria;
import ru.i_novus.integration.monitoring.api.criteria.SentMessageStageCriteria;
import ru.i_novus.integration.monitoring.backend.entity.SentMessageEntity;
import ru.i_novus.integration.monitoring.backend.entity.SentMessageStageEntity;
import ru.i_novus.integration.monitoring.api.model.ErrorModel;
import ru.i_novus.integration.monitoring.api.model.SentMessageModel;
import ru.i_novus.integration.monitoring.api.model.SentMessageStageModel;
import ru.i_novus.integration.monitoring.backend.repository.SentMessageRepository;
import ru.i_novus.integration.monitoring.backend.repository.SentMessageSpecifications;
import ru.i_novus.integration.monitoring.backend.repository.SentMessageStageRepository;
import ru.i_novus.integration.monitoring.backend.repository.SentMessageStageSpecifications;
import ru.i_novus.integration.monitoring.api.service.MonitoringService;

import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class MonitoringServiceImpl implements MonitoringService {

    private SentMessageRepository sentMessageRepository;
    private SentMessageStageRepository sentMessageStageRepository;

    @Override
    public Page<SentMessageModel> findAll(SentMessageCriteria criteria) {
        return findSentMessage(criteria).map(SentMessageEntity::getSentMessageModel);
    }

    @Override
    public Page<SentMessageStageModel> monitoringFormByUid(SentMessageStageCriteria criteria) {
        return findSentMessageStage(criteria).map(SentMessageStageEntity::fillSentMessageStageModel);
    }

    @Override
    public ErrorModel getErrorStackTrace(Integer id) {
        ErrorModel errorModel = new ErrorModel();
        SentMessageStageEntity sentMessageStageEntity = sentMessageStageRepository.findById(id).orElseThrow();
        errorModel.setId(sentMessageStageEntity.getId());
        errorModel.setError(sentMessageStageEntity.getError());
        return errorModel;
    }

    @Override
    public SentMessageStageModel save(MonitoringModel model) {
        Optional<SentMessageEntity> messageEntity = sentMessageRepository.findByUidAndSenderAndReceiver(model.getUid(),
                model.getSender(), model.getReceiver());
        Integer sentMessageId;
        if (messageEntity.isPresent()) {
            messageEntity.get().setCurrentStatus(model.getStatus());
            sentMessageId = messageEntity.get().getId();
        } else {
            sentMessageId = sentMessageRepository.save(new SentMessageEntity(model.getUid(), model.getDateTime(),
                    model.getSender(), model.getReceiver(), model.getOperation(), model.getStatus(), model.getComment())).getId();
        }
        return sentMessageStageRepository.save(new SentMessageStageEntity(sentMessageId, model.getDateTime(), model.getStatus(),
                model.getError(), model.getComment())).fillSentMessageStageModel();

    }

    private Page<SentMessageEntity> findSentMessage(SentMessageCriteria criteria) {
        Pageable pageable = PageRequest.of(
                criteria.getPageNumber(), criteria.getPageSize(),
                (nonNull(criteria.getOrders()) && !criteria.getOrders().isEmpty()) ?
                        Sort.by(criteria.getOrders()) : Sort.by(Sort.Direction.DESC, "sentDateTime")
        );
        return sentMessageRepository.findAll(SentMessageSpecifications.equalCriteriaParams(criteria), pageable);
    }


    private Page<SentMessageStageEntity> findSentMessageStage(SentMessageStageCriteria criteria) {
        Pageable pageable = PageRequest.of(
                criteria.getPageNumber(), criteria.getPageSize(),
                (nonNull(criteria.getOrders()) && !criteria.getOrders().isEmpty()) ?
                        Sort.by(criteria.getOrders()) : Sort.by(Sort.Direction.DESC, "dateTime")
        );
        return sentMessageStageRepository.findAll(SentMessageStageSpecifications.equalCriteriaParams(criteria), pageable);
    }
}
