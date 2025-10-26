package org.fleetwave.app.schedulers;
import org.fleetwave.domain.repo.*; import org.fleetwave.domain.*; import org.fleetwave.app.services.AlertService; import org.springframework.scheduling.annotation.Scheduled; import org.springframework.stereotype.Component; import org.springframework.beans.factory.annotation.Autowired; import java.time.OffsetDateTime;
@Component public class OverdueScanner {
  private final AssignmentRepository assignments; private final AlertRuleRepository rules; private final AlertService alerts;
  @Autowired public OverdueScanner(AssignmentRepository a, AlertRuleRepository r, AlertService s){ assignments=a; rules=r; alerts=s; }
  @Scheduled(fixedDelay=60000) public void scan(){ var now=OffsetDateTime.now(); var list = rules.findByEnabledTrue(); assignments.findOverdue(now).forEach(a->{ for (AlertRule rule: list){ if(rule.getType()==AlertRule.Type.OVERDUE_ASSIGNMENT){ alerts.raise(rule,"Assignment",a.getId()); } } }); }
}