package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name="radio", uniqueConstraints = {
  @UniqueConstraint(columnNames = {"tenant_id","serial"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Radio extends BaseEntity {
  public enum Status { ACTIVE, IN_SERVICE, RETIRED }

  @Id private UUID id;
  @Column(nullable=false) private String serial;
  private String callsign;
  private String model;

  @Enumerated(EnumType.STRING)
  @Column(nullable=false)
  private Status status = Status.ACTIVE;

  @Column(name="created_at", nullable=false)
  private OffsetDateTime createdAt = OffsetDateTime.now();
}
