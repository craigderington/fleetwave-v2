package org.fleetwave.domain.repo;

import org.fleetwave.domain.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AlertRuleRepository extends JpaRepository<AlertRule, UUID> {
  List<AlertRule> findByEnabledTrue();
}
