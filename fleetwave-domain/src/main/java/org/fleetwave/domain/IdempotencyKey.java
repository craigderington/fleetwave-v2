package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name="idempotency_key", uniqueConstraints=@UniqueConstraint(columnNames={"tenant_id","key"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@org.hibernate.annotations.Filter(name="tenant")
public class IdempotencyKey extends BaseEntity {
  @Id private UUID id;
  @Column(nullable=false) private String key;
  @Column(nullable=false) private OffsetDateTime createdAt = OffsetDateTime.now();
}
