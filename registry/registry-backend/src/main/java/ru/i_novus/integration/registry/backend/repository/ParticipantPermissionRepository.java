package ru.i_novus.integration.registry.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.i_novus.integration.registry.backend.entity.ParticipantPermissionEntity;

import java.util.List;

@Repository
public interface ParticipantPermissionRepository extends JpaRepository<ParticipantPermissionEntity, Integer>, JpaSpecificationExecutor<ParticipantPermissionEntity> {
    @Query("select r from ParticipantPermissionEntity r " +
            "where r.participantMethodId = ?1 and (r.participantCode = ?2 or r.groupCode = ?3)")
    List<ParticipantPermissionEntity> find(@Param("participantMethodId") Integer participantMethodId,
                                           @Param("participantCode") String participantCode, @Param("groupCode") String groupCode);
}
