package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name="workgroup", uniqueConstraints = {
  @UniqueConstraint(columnNames = {"tenant_id","department_id","name"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Workgroup extends BaseEntity {
  @Id private UUID id;

  @ManyToOne(optional=false)
  @JoinColumn(name="department_id")
  private Department department;

  @Column(nullable=false) private String name;
}
