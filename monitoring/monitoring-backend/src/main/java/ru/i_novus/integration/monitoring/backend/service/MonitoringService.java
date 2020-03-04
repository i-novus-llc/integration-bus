package ru.i_novus.integration.monitoring.backend.service;

import org.springframework.data.domain.Page;
import ru.i_novus.integration.monitoring.backend.MonitoringCriteria;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.backend.model.MonitoringStageModel;

public interface MonitoringService {
    Page<MonitoringModel> findAll(MonitoringCriteria monitoringCriteria);
    Page<MonitoringStageModel> monitoringFormByUid(MonitoringCriteria criteria);
    String getErrorStackTrace(Integer id);
}
