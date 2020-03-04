package ru.i_novus.integration.monitoring.backend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.i_novus.integration.monitoring.backend.MonitoringCriteria;
import ru.i_novus.integration.common.api.model.MonitoringModel;
import ru.i_novus.integration.monitoring.backend.entity.MonitoringEntity;
import ru.i_novus.integration.monitoring.backend.model.MonitoringStageModel;
import ru.i_novus.integration.monitoring.backend.repository.MonitoringRepository;
import ru.i_novus.integration.monitoring.backend.repository.MonitoringSpecifications;
import ru.i_novus.integration.monitoring.backend.service.MonitoringService;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class MonitoringServiceImpl implements MonitoringService {

    private MonitoringRepository repository;

    @Override
    public Page<MonitoringModel> findAll(MonitoringCriteria criteria) {
        return findMonitoring(criteria).map(MonitoringEntity::getMonitoringModel);
    }

    @Override
    public Page<MonitoringStageModel> monitoringFormByUid(MonitoringCriteria criteria) {
        return findMonitoring(criteria).map(MonitoringEntity::fillMonitoringStageModel);
    }

    @Override
    public String getErrorStackTrace(Integer id) {
        return repository.findById(id).orElseThrow().getError();
    }


    private Page<MonitoringEntity> findMonitoring(MonitoringCriteria criteria) {
        Pageable pageable = PageRequest.of(
                criteria.getPageNumber(), criteria.getPageSize(),
                (nonNull(criteria.getOrders()) && !criteria.getOrders().isEmpty()) ?
                        Sort.by(criteria.getOrders()) : Sort.by(Sort.Direction.DESC, "dateTime")
        );
        return repository.findAll(MonitoringSpecifications.equalCriteriaParams(criteria), pageable);
    }
}
