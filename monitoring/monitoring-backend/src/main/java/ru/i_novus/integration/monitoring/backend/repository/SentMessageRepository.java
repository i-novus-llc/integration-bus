package ru.i_novus.integration.monitoring.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.i_novus.integration.monitoring.backend.entity.SentMessageEntity;

import java.util.Optional;

@Repository
public interface SentMessageRepository extends JpaRepository<SentMessageEntity, Integer>,
        JpaSpecificationExecutor<SentMessageEntity> {

    Optional<SentMessageEntity> findByUidAndSenderAndReceiver(String uid, String sender, String receiver);
}
