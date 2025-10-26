package org.fleetwave.app.services;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.AlertRepository;
import org.fleetwave.domain.repo.AlertRuleRepository;
import org.fleetwave.domain.repo.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class AlertService {
  private final AlertRepository alerts;
  private final AlertRuleRepository rules;
  private final NotificationRepository notifications;

  @Transactional
  public Alert raise(AlertRule rule, String subjectType, UUID subjectId){
    var existing = alerts.findByRule_IdAndSubjectTypeAndSubjectIdAndStatus(rule.getId(), subjectType, subjectId, Alert.Status.OPEN);
    if (existing.isPresent()){
      var a = existing.get();
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
