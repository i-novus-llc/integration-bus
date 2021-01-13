package ru.i_novus.integration.monitoring.api.service;

import org.springframework.data.domain.Page;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.api.criteria.SentMessageCriteria;
import ru.i_novus.integration.monitoring.api.criteria.SentMessageStageCriteria;
import ru.i_novus.integration.monitoring.api.model.ErrorModel;
import ru.i_novus.integration.monitoring.api.model.SentMessageModel;
import ru.i_novus.integration.monitoring.api.model.SentMessageStageModel;

public interface MonitoringService {
    Page<SentMessageModel> findAll(SentMessageCriteria sentMessageCriteria);

    Page<SentMessageStageModel> monitoringFormByUid(SentMessageStageCriteria criteria);

    ErrorModel getErrorStackTrace(Integer id);

    SentMessageStageModel save(MonitoringModel monitoringModel);
}
