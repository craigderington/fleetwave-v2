package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name="request")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Request extends BaseEntity {
  public enum Status { PENDING, APPROVED, REJECTED, FULFILLED, CANCELLED }

  @Id private UUID id;
  @ManyToOne(optional=false) @JoinColumn(name="workgroup_id") private Workgroup workgroup;
  @Column(nullable=false) private UUID requesterId; // user id (future FK to users table)
  private String radioModelPref;
  private String reason;
  private OffsetDateTime neededFrom;
  private OffsetDateTime neededUntil;

  @Enumerated(EnumType.STRING) @Column(nullable=false)
  private Status status = Status.PENDING;

  @Column(nullable=false) private OffsetDateTime createdAt = OffsetDateTime.now();
}
