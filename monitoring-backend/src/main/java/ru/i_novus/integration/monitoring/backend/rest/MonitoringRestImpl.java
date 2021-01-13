package ru.i_novus.integration.monitoring.backend.rest;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.api.criteria.SentMessageCriteria;
import ru.i_novus.integration.monitoring.api.criteria.SentMessageStageCriteria;
import ru.i_novus.integration.monitoring.api.model.ErrorModel;
import ru.i_novus.integration.monitoring.api.model.SentMessageModel;
import ru.i_novus.integration.monitoring.api.model.SentMessageStageModel;
import ru.i_novus.integration.monitoring.api.rest.MonitoringRest;
import ru.i_novus.integration.monitoring.api.service.MonitoringService;

@AllArgsConstructor
@Controller
public class MonitoringRestImpl implements MonitoringRest {

    private MonitoringService service;

    @Override
    public Page<SentMessageModel> findAll(SentMessageCriteria criteria) {
        return service.findAll(criteria);
    }

    @Override
    public SentMessageStageModel save(MonitoringModel model) {
        return service.save(model);
    }

    @Override
    public Page<SentMessageStageModel> findByUid(SentMessageStageCriteria criteria) {
        return service.monitoringFormByUid(criteria);
    }

    @Override
    public ErrorModel getErrorStackTrace(Integer id) {
        return service.getErrorStackTrace(id);
    }

}
