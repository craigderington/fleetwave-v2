package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name="alert")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alert extends BaseEntity {
  public enum Status { OPEN, ACK, CLOSED }

  @Id private UUID id;
  @ManyToOne(optional=false) @JoinColumn(name="rule_id") private AlertRule rule;
  @Column(nullable=false) private String subjectType;
  @Column(nullable=false) private UUID subjectId;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status = Status.OPEN;
  @Column(nullable=false) private OffsetDateTime firstSeen = OffsetDateTime.now();
  @Column(nullable=false) private OffsetDateTime lastSeen = OffsetDateTime.now();
  @Column(nullable=false) private int count = 1;
}
