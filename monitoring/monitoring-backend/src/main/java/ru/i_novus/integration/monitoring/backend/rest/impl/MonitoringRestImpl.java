package ru.i_novus.integration.monitoring.backend.rest.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.backend.MonitoringCriteria;
import ru.i_novus.integration.monitoring.backend.model.MonitoringFormModel;
import ru.i_novus.integration.monitoring.backend.model.MonitoringStageModel;
import ru.i_novus.integration.monitoring.backend.rest.MonitoringRest;
import ru.i_novus.integration.monitoring.backend.service.MonitoringService;

@AllArgsConstructor
@Controller
public class MonitoringRestImpl implements MonitoringRest {

    private MonitoringService service;

    @Override
    public Page<MonitoringModel> findAll(MonitoringCriteria criteria) {
        return service.findAll(criteria);
    }

    @Override
    public Page<MonitoringStageModel> findByUid(MonitoringCriteria criteria) {
        return service.monitoringFormByUid(criteria);
    }

    @Override
    public MonitoringFormModel fillHeader(String uid, String sender, String receiver, String operation) {
        return new MonitoringFormModel(uid, sender, receiver, operation);
    }

    @Override
    public String getErrorStackTrace(Integer id) {
        return service.getErrorStackTrace(id);
    }

}
