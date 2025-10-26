package org.fleetwave.app.schedulers;

import org.fleetwave.app.services.AlertService;
import org.fleetwave.domain.AlertRule;
import org.fleetwave.domain.repo.AlertRuleRepository;
import org.fleetwave.domain.repo.AssignmentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.OffsetDateTime;

@Component
public class OverdueScanner {
  private final AssignmentRepository assignments;
  private final AlertRuleRepository rules;
  private final AlertService alerts;

  @Autowired
  public OverdueScanner(AssignmentRepository assignments, AlertRuleRepository rules, AlertService alerts){
    this.assignments = assignments; this.rules = rules; this.alerts = alerts;
  }

  @Scheduled(fixedDelayString = "${alerts.overdue.pollMs:60000}")
  @Transactional
  public void run(){
    OffsetDateTime now = OffsetDateTime.now();
    AlertRule rule = rules.findByEnabledTrue().stream()
      .filter(r -> r.getType() == AlertRule.Type.OVERDUE_ASSIGNMENT)
      .findFirst().orElse(null);
    if (rule == null) return;
    assignments.findOverdue(now).forEach(a -> alerts.raise(rule, "ASSIGNMENT", a.getId()));
  }
}
