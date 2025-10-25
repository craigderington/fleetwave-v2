package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity @Table(name = "tenant")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tenant {
  @Id private UUID id;
  @Column(nullable=false, unique=true) private String key;
  @Column(nullable=false) private String name;
}
