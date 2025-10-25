package org.fleetwave.domain.repo;

import org.fleetwave.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
  Optional<Alert> findByRule_IdAndSubjectTypeAndSubjectIdAndStatus(UUID ruleId, String subjectType, UUID subjectId, Alert.Status status);
}
