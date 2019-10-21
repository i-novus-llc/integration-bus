package ru.i_novus.integration.registry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.i_novus.integration.registry.backend.entity.ParticipantEntity;

import java.util.Optional;


@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, String>, JpaSpecificationExecutor<ParticipantEntity> {
    @Query("select r from ParticipantEntity r where (r.disable = false or r.disable is null) and r.code = ?1")
    Optional<ParticipantEntity> findEnabledById(@Param("code") String code);

    Optional<ParticipantEntity> findById(@Param("code") String code);
}

