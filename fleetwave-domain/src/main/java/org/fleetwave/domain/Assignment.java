package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name="assignment")
public class Assignment {
  public enum Status { ACTIVE, RETURNED, CANCELLED }
  public enum AssigneeType { USER, WORKGROUP }
  @Id private UUID id;
  @ManyToOne @JoinColumn(name="radio_id") private Radio radio;
  @Enumerated(EnumType.STRING) private AssigneeType assigneeType;
  private UUID assigneeId;
  private OffsetDateTime startAt = OffsetDateTime.now();
  private OffsetDateTime expectedEnd;
  private OffsetDateTime endAt;
  @Enumerated(EnumType.STRING) private Status status = Status.ACTIVE;

}
