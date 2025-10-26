package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="request")
public class Request {
  public enum Status { PENDING, APPROVED, REJECTED, FULFILLED, CANCELLED }

  @Id
  private UUID id;

  @ManyToOne
  @JoinColumn(name="workgroup_id")
  private Workgroup workgroup;

  private UUID requesterId;
  private String radioModelPref;
  private String reason;
  private OffsetDateTime neededUntil;

  @Enumerated(EnumType.STRING)
  private Status status = Status.PENDING;

  private OffsetDateTime createdAt = OffsetDateTime.now();

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public Workgroup getWorkgroup() { return workgroup; }
  public void setWorkgroup(Workgroup workgroup) { this.workgroup = workgroup; }
  public UUID getRequesterId() { return requesterId; }
  public void setRequesterId(UUID requesterId) { this.requesterId = requesterId; }
  public String getRadioModelPref() { return radioModelPref; }
  public void setRadioModelPref(String radioModelPref) { this.radioModelPref = radioModelPref; }
  public String getReason() { return reason; }
  public void setReason(String reason) { this.reason = reason; }
  public OffsetDateTime getNeededUntil() { return neededUntil; }
  public void setNeededUntil(OffsetDateTime neededUntil) { this.neededUntil = neededUntil; }
  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

}
