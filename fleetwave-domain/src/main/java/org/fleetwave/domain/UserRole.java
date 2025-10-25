package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name="user_role", uniqueConstraints=@UniqueConstraint(columnNames={"tenant_id","user_id","role_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@org.hibernate.annotations.Filter(name="tenant")
public class UserRole extends BaseEntity {
  @Id private UUID id;
  @ManyToOne(optional=false) @JoinColumn(name="user_id") private User user;
  @ManyToOne(optional=false) @JoinColumn(name="role_id") private Role role;
}
