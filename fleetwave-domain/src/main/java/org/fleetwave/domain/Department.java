package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name="department", uniqueConstraints = {
  @UniqueConstraint(columnNames = {"tenant_id","name"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Department extends BaseEntity {
  @Id private UUID id;
  @Column(nullable=false) private String name;
}
