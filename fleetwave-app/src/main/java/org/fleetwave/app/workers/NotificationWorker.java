package org.fleetwave.app.workers;

import org.fleetwave.app.notif.EmailMessage;
import org.fleetwave.app.notif.EmailSender;
import org.fleetwave.app.notif.SmsSender;
import org.fleetwave.domain.Notification;
import org.fleetwave.domain.repo.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.OffsetDateTime;
import java.util.List;

@Component
public class NotificationWorker {
  private final NotificationRepository repo;
  private final EmailSender email;
  private final SmsSender sms;

  @Autowired
  public NotificationWorker(NotificationRepository repo, EmailSender email, SmsSender sms){
    this.repo = repo; this.email = email; this.sms = sms;
  }

  @Scheduled(fixedDelayString = "${notifications.pollMs:10000}")
  @Transactional
  public void deliver(){
    List<Notification> batch = repo.findTop50ByStatusOrderByLastAttemptAtAsc(Notification.Status.PENDING);
    for (Notification n : batch){
      try {
        switch (n.getChannel()){
          case EMAIL -> email.send(new EmailMessage(null, n.getDestination(), "[FleetWave] Alert", "<p>New alert</p>", "New alert", null));
          case SMS -> sms.send(n.getDestination(), "[FleetWave] Alert");
          default -> {}
        }
        n.setStatus(Notification.Status.SENT);
      } catch (Exception e){
        n.setStatus(Notification.Status.FAILED);
      } finally {
        n.setAttempts(n.getAttempts()+1);
        n.setLastAttemptAt(OffsetDateTime.now());
      }
    }
  }
}
