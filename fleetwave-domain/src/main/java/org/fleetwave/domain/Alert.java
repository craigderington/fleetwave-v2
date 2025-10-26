package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name="alert")
public class Alert {
  public enum Status { OPEN, ACK, CLOSED }
  @Id private UUID id;
  @ManyToOne @JoinColumn(name="rule_id") private AlertRule rule;
  private String subjectType;
  private UUID subjectId;
  @Enumerated(EnumType.STRING) private Status status = Status.OPEN;
  private OffsetDateTime firstSeen = OffsetDateTime.now();
  private OffsetDateTime lastSeen = OffsetDateTime.now();
  private int count = 1;

}
