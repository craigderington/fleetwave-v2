package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name="app_user", uniqueConstraints=@UniqueConstraint(columnNames={"tenant_id","email"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@org.hibernate.annotations.Filter(name="tenant")
public class User extends BaseEntity {
  @Id private UUID id;
  @Column(nullable=false) private String email;
  @Column(nullable=false) private String name;
  @Column(nullable=false) private String passwordHash;
  @Column(nullable=true)  private String phone;
  @Column(nullable=false) private boolean enabled = true;
}
