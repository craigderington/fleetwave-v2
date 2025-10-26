package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name="workgroup")
public class Workgroup {
  @Id private UUID id;
  private String name;

}
