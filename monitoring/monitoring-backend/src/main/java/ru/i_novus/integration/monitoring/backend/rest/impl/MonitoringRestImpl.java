package ru.i_novus.integration.monitoring.backend.rest.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.backend.criteria.SentMessageCriteria;
import ru.i_novus.integration.monitoring.backend.criteria.SentMessageStageCriteria;
import ru.i_novus.integration.monitoring.backend.model.ErrorModel;
import ru.i_novus.integration.monitoring.backend.model.SentMessageModel;
import ru.i_novus.integration.monitoring.backend.model.SentMessageStageModel;
import ru.i_novus.integration.monitoring.backend.rest.MonitoringRest;
import ru.i_novus.integration.monitoring.backend.service.MonitoringService;

@AllArgsConstructor
@Controller
public class MonitoringRestImpl implements MonitoringRest {

    private MonitoringService service;

    @Override
    public Page<SentMessageModel> findAll(SentMessageCriteria criteria) {
        return service.findAll(criteria);
    }

    @Override
    public void getServiceInfo(@RequestBody MonitoringModel model) {
        service.save(model);
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
