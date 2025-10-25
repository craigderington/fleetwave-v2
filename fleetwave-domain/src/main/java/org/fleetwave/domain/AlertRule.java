package org.fleetwave.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name="alert_rule", uniqueConstraints=@UniqueConstraint(columnNames={"tenant_id","name"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlertRule extends BaseEntity {
  public enum Type { OVERDUE_ASSIGNMENT, MAINTENANCE_DUE, CUSTOM_DSL }
  public enum Severity { INFO, WARN, CRITICAL }

  @Id private UUID id;
  @Column(nullable=false) private String name;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Type type;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private Severity severity = Severity.WARN;
  @Column(nullable=false) private boolean enabled = true;
  @Column(nullable=false) private int cooldownSec = 900;
  @Column(columnDefinition="jsonb", nullable=false) private String configJson = "{}";
}
