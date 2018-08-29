package ru.i_novus.integration.monitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.i_novus.integration.monitoring.entity.MonitoringEntity;

@Repository
public interface MonitoringRepository extends JpaRepository<MonitoringEntity, Integer> {
}
