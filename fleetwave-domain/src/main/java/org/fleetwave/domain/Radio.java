package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name="radio")
public class Radio {
  @Id private UUID id;
  private String serial;
  private String model;
  private String callsign;
  @Enumerated(EnumType.STRING) private Status status = Status.ACTIVE;
  public enum Status { ACTIVE, SERVICE, RETIRED }

}
