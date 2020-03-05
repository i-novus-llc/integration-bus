package ru.i_novus.integration.monitoring.backend.service;

import org.springframework.data.domain.Page;
import ru.i_novus.integration.monitoring.backend.criteria.SentMessageCriteria;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.backend.criteria.SentMessageStageCriteria;
import ru.i_novus.integration.monitoring.backend.model.ErrorModel;
import ru.i_novus.integration.monitoring.backend.model.SentMessageModel;
import ru.i_novus.integration.monitoring.backend.model.SentMessageStageModel;

public interface MonitoringService {
    Page<SentMessageModel> findAll(SentMessageCriteria sentMessageCriteria);
    Page<SentMessageStageModel> monitoringFormByUid(SentMessageStageCriteria criteria);
    ErrorModel getErrorStackTrace(Integer id);
    void save(MonitoringModel monitoringModel);
}
