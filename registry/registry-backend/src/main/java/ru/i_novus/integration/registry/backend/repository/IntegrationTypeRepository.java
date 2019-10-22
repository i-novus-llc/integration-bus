package ru.i_novus.integration.registry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.i_novus.integration.registry.backend.entity.IntegrationTypeEntity;

@Repository
public interface IntegrationTypeRepository extends JpaRepository<IntegrationTypeEntity, String>, JpaSpecificationExecutor<IntegrationTypeEntity> {
}
