package org.fleetwave.app.services;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.*;
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
    var a = Alert.builder()
      .id(UUID.randomUUID())
      .rule(rule).subjectType(subjectType).subjectId(subjectId)
      .status(Alert.Status.OPEN)
      .build();
    alerts.save(a);
    // enqueue default notifications (email to dispatch@{tenant} placeholder, SMS none here)
    notifications.save(Notification.builder()
      .id(UUID.randomUUID()).alert(a).channel(Notification.Channel.EMAIL)
      .destination("dispatch@tenant.local").status(Notification.Status.PENDING).build());
    return a;
  }
}
