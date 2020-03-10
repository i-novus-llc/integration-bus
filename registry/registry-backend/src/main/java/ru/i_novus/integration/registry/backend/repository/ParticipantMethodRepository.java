package ru.i_novus.integration.registry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.i_novus.integration.registry.backend.entity.ParticipantMethodEntity;

import java.util.Optional;

@Repository
public interface ParticipantMethodRepository extends JpaRepository<ParticipantMethodEntity, Integer>, JpaSpecificationExecutor<ParticipantMethodEntity> {
    @Query("select r from ParticipantMethodEntity r where (r.disable = false or r.disable is null) and r.participantCode = ?1 and r.methodCode = ?2")
    Optional<ParticipantMethodEntity> findEnabled(@Param("participantCode") String participantCode, @Param("methodCode") String methodCode);
}
