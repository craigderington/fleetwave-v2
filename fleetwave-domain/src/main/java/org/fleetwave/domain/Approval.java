package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name="approval")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Approval extends BaseEntity {
  public enum Decision { APPROVE, REJECT }
  @Id private UUID id;
  @ManyToOne(optional=false) @JoinColumn(name="request_id") private Request request;
  @Column(nullable=false) private UUID approverId;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Decision decision;
  private String comment;
  @Column(nullable=false) private OffsetDateTime decidedAt = OffsetDateTime.now();
}
