package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="assignment")
public class Assignment {
  public enum Status { ACTIVE, RETURNED, CANCELLED }
  public enum AssigneeType { USER, WORKGROUP }

  @Id
  private UUID id;

  @ManyToOne
  @JoinColumn(name="radio_id")
  private Radio radio;

  @Enumerated(EnumType.STRING)
  private AssigneeType assigneeType;

  private UUID assigneeId;
  private OffsetDateTime startAt = OffsetDateTime.now();
  private OffsetDateTime expectedEnd;
  private OffsetDateTime endAt;

  @Enumerated(EnumType.STRING)
  private Status status = Status.ACTIVE;

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public Radio getRadio() { return radio; }
  public void setRadio(Radio radio) { this.radio = radio; }
  public AssigneeType getAssigneeType() { return assigneeType; }
  public void setAssigneeType(AssigneeType assigneeType) { this.assigneeType = assigneeType; }
  public UUID getAssigneeId() { return assigneeId; }
  public void setAssigneeId(UUID assigneeId) { this.assigneeId = assigneeId; }
  public OffsetDateTime getStartAt() { return startAt; }
  public void setStartAt(OffsetDateTime startAt) { this.startAt = startAt; }
  public OffsetDateTime getExpectedEnd() { return expectedEnd; }
  public void setExpectedEnd(OffsetDateTime expectedEnd) { this.expectedEnd = expectedEnd; }
  public OffsetDateTime getEndAt() { return endAt; }
  public void setEndAt(OffsetDateTime endAt) { this.endAt = endAt; }
  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }

}
