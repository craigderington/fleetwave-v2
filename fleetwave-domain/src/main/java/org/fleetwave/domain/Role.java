package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name="role", uniqueConstraints=@UniqueConstraint(columnNames={"tenant_id","name"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@org.hibernate.annotations.Filter(name="tenant")
public class Role extends BaseEntity {
  @Id private UUID id;
  @Column(nullable=false) private String name; // ADMIN, DISPATCHER, TECHNICIAN, SUPERVISOR, MANAGER
}
