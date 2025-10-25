package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name="notification")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification extends BaseEntity {
  public enum Channel { EMAIL, SMS, WEBHOOK }
  public enum Status { PENDING, SENT, FAILED }

  @Id private UUID id;
  @ManyToOne(optional=false) @JoinColumn(name="alert_id") private Alert alert;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Channel channel;
  @Column(nullable=false) private String destination;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status = Status.PENDING;
  @Column(nullable=false) private int attempts = 0;
  private OffsetDateTime lastAttemptAt;
}
