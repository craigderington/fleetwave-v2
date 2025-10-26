package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name="alertrule")
public class AlertRule {
  public enum Type { OVERDUE_ASSIGNMENT, MAINTENANCE_DUE, CUSTOM_DSL }
  public enum Severity { INFO, WARN, CRITICAL }
  @Id private UUID id;
  private String name;
  @Enumerated(EnumType.STRING) private Type type = Type.OVERDUE_ASSIGNMENT;
  @Enumerated(EnumType.STRING) private Severity severity = Severity.WARN;
  private boolean enabled = true;
  private int cooldownSec = 900;
  @Column(length=4000) private String configJson = "{}";

}
