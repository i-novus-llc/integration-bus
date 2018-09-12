package ru.i_novus.integration.registry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.i_novus.integration.registry.backend.entity.RegistryEntity;

import java.util.Optional;


@Repository
public interface RegistryRepository extends JpaRepository<RegistryEntity, String> {
    @Query("select r from RegistryEntity r where (r.disable = false or r.disable is null) and r.code = ?1")
    Optional<RegistryEntity> findById(@Param("code") String code);
}

