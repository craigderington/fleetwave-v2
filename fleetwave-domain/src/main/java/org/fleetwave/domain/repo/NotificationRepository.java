package org.fleetwave.domain.repo;
import org.fleetwave.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
  List<Notification> findTop50ByStatusOrderByLastAttemptAtAsc(Notification.Status status);
}
