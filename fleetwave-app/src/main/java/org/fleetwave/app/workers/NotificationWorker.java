package org.fleetwave.app.workers;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.Notification;
import org.fleetwave.domain.repo.NotificationRepository;
import org.fleetwave.infra.notif.EmailMessage;
import org.fleetwave.infra.notif.EmailSender;
import org.fleetwave.infra.notif.SmsSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Component @RequiredArgsConstructor
public class NotificationWorker {

  private final JdbcTemplate jdbc;

  private boolean acquireLeader(String name){
    Long ok = jdbc.queryForObject("select case when pg_try_advisory_lock(hashtext(?)) then 1 else 0 end", Long.class, name);
    return ok != null && ok == 1L;
  }

  private final NotificationRepository repo;
  private final EmailSender email;
  private final SmsSender sms;

  @Scheduled(fixedDelayString = "${notifications.pollMs:10000}")
  @Transactional
  public void deliver(){
    if (!acquireLeader("NotificationWorker.java")) return;

    var batch = repo.findTop50ByStatusOrderByLastAttemptAtAsc(Notification.Status.PENDING);
    for (var n : batch){
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
