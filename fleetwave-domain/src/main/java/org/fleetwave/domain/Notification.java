package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name="notification")
public class Notification {
  public enum Channel { EMAIL, SMS, WEBHOOK }
  public enum Status { PENDING, SENT, FAILED }
  @Id private UUID id;
  @ManyToOne @JoinColumn(name="alert_id") private Alert alert;
  @Enumerated(EnumType.STRING) private Channel channel = Channel.EMAIL;
  private String destination;
  @Enumerated(EnumType.STRING) private Status status = Status.PENDING;
  private int attempts = 0;
  private java.time.OffsetDateTime lastAttemptAt;

}
