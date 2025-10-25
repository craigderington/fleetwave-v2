package org.fleetwave.app.schedulers;

import lombok.RequiredArgsConstructor;
import org.fleetwave.app.services.AlertService;
import org.fleetwave.domain.AlertRule;
import org.fleetwave.domain.repo.AlertRuleRepository;
import org.fleetwave.domain.repo.AssignmentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Component @RequiredArgsConstructor
public class OverdueScanner {

  private final JdbcTemplate jdbc;

  private boolean acquireLeader(String name){
    Long ok = jdbc.queryForObject("select case when pg_try_advisory_lock(hashtext(?)) then 1 else 0 end", Long.class, name);
    return ok != null && ok == 1L;
  }

  private final AssignmentRepository assignments;
  private final AlertRuleRepository rules;
  private final AlertService alerts;

  @Scheduled(fixedDelayString = "${alerts.overdue.pollMs:60000}")
  @Transactional
  public void run(){
    if (!acquireLeader("OverdueScanner.java")) return;

    var now = OffsetDateTime.now();
    var rule = rules.findByEnabledTrue().stream()
      .filter(r -> r.getType() == AlertRule.Type.OVERDUE_ASSIGNMENT)
      .findFirst().orElseGet(() -> null);
    if (rule == null) return;

    assignments.findOverdue(now).forEach(a -> {
      alerts.raise(rule, "ASSIGNMENT", a.getId());
    });
  }
}
