package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name="assignment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Assignment extends BaseEntity {

  public enum AssigneeType { USER, WORKGROUP }
  public enum Status { ACTIVE, RETURNED, CANCELLED }

  @Id private UUID id;

  @ManyToOne(optional=false) @JoinColumn(name="radio_id")
  private Radio radio;

  @Enumerated(EnumType.STRING) @Column(name="assignee_type", nullable=false)
  private AssigneeType assigneeType;

  @Column(name="assignee_id", nullable=false)
  private UUID assigneeId;

  @Column(name="start_at", nullable=false)
  private OffsetDateTime startAt = OffsetDateTime.now();

  @Column(name="expected_end")
  private OffsetDateTime expectedEnd;

  @Column(name="end_at")
  private OffsetDateTime endAt;

  @Enumerated(EnumType.STRING) @Column(nullable=false)
  private Status status = Status.ACTIVE;
}
