package ru.i_novus.integration.monitoring.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.i_novus.integration.monitoring.backend.entity.MonitoringEntity;

@Repository
public interface MonitoringRepository extends JpaRepository<MonitoringEntity, Integer>,
        JpaSpecificationExecutor<MonitoringEntity> {
}
