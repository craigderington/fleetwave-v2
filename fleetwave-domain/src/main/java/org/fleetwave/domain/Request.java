package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name="request")
public class Request {
  public enum Status { PENDING, APPROVED, REJECTED, FULFILLED, CANCELLED }
  @Id private UUID id;
  @ManyToOne @JoinColumn(name="workgroup_id") private Workgroup workgroup;
  private UUID requesterId;
  private String radioModelPref;
  private String reason;
  private OffsetDateTime neededUntil;
  @Enumerated(EnumType.STRING) private Status status = Status.PENDING;
  private OffsetDateTime createdAt = OffsetDateTime.now();

}
