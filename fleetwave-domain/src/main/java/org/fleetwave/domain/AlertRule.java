package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="alertrule")
public class AlertRule {
  public enum Type { OVERDUE_ASSIGNMENT, MAINTENANCE_DUE, CUSTOM_DSL }
  public enum Severity { INFO, WARN, CRITICAL }

  @Id
  private UUID id;

  private String name;

  @Enumerated(EnumType.STRING)
  private Type type = Type.OVERDUE_ASSIGNMENT;

  @Enumerated(EnumType.STRING)
  private Severity severity = Severity.WARN;

  private boolean enabled = true;
  private int cooldownSec = 900;

  @Column(length=4000)
  private String configJson = "{}";

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public Type getType() { return type; }
  public void setType(Type type) { this.type = type; }
  public Severity getSeverity() { return severity; }
  public void setSeverity(Severity severity) { this.severity = severity; }
  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean enabled) { this.enabled = enabled; }
  public int getCooldownSec() { return cooldownSec; }
  public void setCooldownSec(int cooldownSec) { this.cooldownSec = cooldownSec; }
  public String getConfigJson() { return configJson; }
  public void setConfigJson(String configJson) { this.configJson = configJson; }

}
