package org.fleetwave.app.services;
import org.fleetwave.domain.*; import org.fleetwave.domain.repo.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import org.springframework.beans.factory.annotation.Autowired; import java.time.OffsetDateTime; import java.util.*; @Service public class AlertService{
  private final AlertRepository alerts; private final NotificationRepository notifications;
  @Autowired public AlertService(AlertRepository a, NotificationRepository n){ alerts=a; notifications=n; }
  @Transactional public Alert raise(AlertRule rule, String subjectType, java.util.UUID subjectId){
    Optional<Alert> existing = alerts.findByRule_IdAndSubjectTypeAndSubjectIdAndStatus(rule.getId(), subjectType, subjectId, Alert.Status.OPEN);
    if(existing.isPresent()){ Alert a = existing.get(); a.setLastSeen(OffsetDateTime.now()); a.setCount(a.getCount()+1); return a; }
    Alert a = new Alert(); a.setId(java.util.UUID.randomUUID()); a.setRule(rule); a.setSubjectType(subjectType); a.setSubjectId(subjectId); a.setStatus(Alert.Status.OPEN); alerts.save(a);
    Notification n = new Notification(); n.setId(java.util.UUID.randomUUID()); n.setAlert(a); n.setChannel(Notification.Channel.EMAIL); n.setDestination("dispatch@tenant.local"); n.setStatus(Notification.Status.PENDING); notifications.save(n);
    return a;
  }
}