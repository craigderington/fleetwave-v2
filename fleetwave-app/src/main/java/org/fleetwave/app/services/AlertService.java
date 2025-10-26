package org.fleetwave.app.services;

import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.AlertRepository;
import org.fleetwave.domain.repo.AlertRuleRepository;
import org.fleetwave.domain.repo.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.Optional;

@Service
public class AlertService {
  private final AlertRepository alerts;
  private final AlertRuleRepository rules;
  private final NotificationRepository notifications;

  @Autowired
  public AlertService(AlertRepository alerts, AlertRuleRepository rules, NotificationRepository notifications){
    this.alerts = alerts; this.rules = rules; this.notifications = notifications;
  }

  @Transactional
  public Alert raise(AlertRule rule, String subjectType, UUID subjectId){
    Optional<Alert> existing = alerts.findByRule_IdAndSubjectTypeAndSubjectIdAndStatus(rule.getId(), subjectType, subjectId, Alert.Status.OPEN);
    if (existing.isPresent()){
      Alert a = existing.get();
      a.setLastSeen(OffsetDateTime.now());
      a.setCount(a.getCount()+1);
      return a;
    }
    Alert a = new Alert();
    a.setId(UUID.randomUUID());
    a.setRule(rule);
    a.setSubjectType(subjectType);
    a.setSubjectId(subjectId);
    a.setStatus(Alert.Status.OPEN);
    alerts.save(a);

    Notification n = new Notification();
    n.setId(UUID.randomUUID());
    n.setAlert(a);
    n.setChannel(Notification.Channel.EMAIL);
    n.setDestination("dispatch@tenant.local");
    n.setStatus(Notification.Status.PENDING);
    notifications.save(n);
    return a;
  }
}
