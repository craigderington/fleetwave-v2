package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name="work_order")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkOrder extends BaseEntity {
  public enum Status { OPEN, IN_PROGRESS, CLOSED }
  @Id private UUID id;
  @ManyToOne(optional=false) @JoinColumn(name="radio_id") private Radio radio;
  private String planName;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status = Status.OPEN;
  @Column(nullable=false) private OffsetDateTime openedAt = OffsetDateTime.now();
  private OffsetDateTime closedAt;
}
