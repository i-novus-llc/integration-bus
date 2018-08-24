package ru.i_novus.is.registry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.i_novus.is.registry.backend.entity.RegistryEntity;

@Repository
public interface RegistryRepository extends JpaRepository<RegistryEntity, String> {
}
