package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="alert")
public class Alert {
  public enum Status { OPEN, ACK, CLOSED }

  @Id
  private UUID id;

  @ManyToOne
  @JoinColumn(name="rule_id")
  private AlertRule rule;

  private String subjectType;
  private UUID subjectId;

  @Enumerated(EnumType.STRING)
  private Status status = Status.OPEN;

  private OffsetDateTime firstSeen = OffsetDateTime.now();
  private OffsetDateTime lastSeen = OffsetDateTime.now();
  private int count = 1;

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public AlertRule getRule() { return rule; }
  public void setRule(AlertRule rule) { this.rule = rule; }
  public String getSubjectType() { return subjectType; }
  public void setSubjectType(String subjectType) { this.subjectType = subjectType; }
  public UUID getSubjectId() { return subjectId; }
  public void setSubjectId(UUID subjectId) { this.subjectId = subjectId; }
  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }
  public OffsetDateTime getFirstSeen() { return firstSeen; }
  public void setFirstSeen(OffsetDateTime firstSeen) { this.firstSeen = firstSeen; }
  public OffsetDateTime getLastSeen() { return lastSeen; }
  public void setLastSeen(OffsetDateTime lastSeen) { this.lastSeen = lastSeen; }
  public int getCount() { return count; }
  public void setCount(int count) { this.count = count; }

}
