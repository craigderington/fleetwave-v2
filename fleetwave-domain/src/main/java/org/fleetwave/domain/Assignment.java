package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "assignments")
public class Assignment {

  public enum Status {
    ACTIVE,
    RETURNED
  }

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "tenant_id", nullable = false, length = 100)
  private String tenantId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "radio_id")
  private Radio radio;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignee_person_id")
  private Person assigneePerson;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignee_workgroup_id")
  private Workgroup assigneeWorkgroup;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private Status status;

  @Column(name = "start_at", nullable = false)
  private OffsetDateTime startAt;

  @Column(name = "expected_end")
  private OffsetDateTime expectedEnd;

  @Column(name = "end_at")
  private OffsetDateTime endAt;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  public Assignment() {}

  // -------- Getters --------

  public UUID getId() {
    return id;
  }

  public String getTenantId() {
    return tenantId;
  }

  public Radio getRadio() {
    return radio;
  }

  public Person getAssigneePerson() {
    return assigneePerson;
  }

  public Workgroup getAssigneeWorkgroup() {
    return assigneeWorkgroup;
  }

  public Status getStatus() {
    return status;
  }

  public OffsetDateTime getStartAt() {
    return startAt;
  }

  public OffsetDateTime getExpectedEnd() {
    return expectedEnd;
  }

  public OffsetDateTime getEndAt() {
    return endAt;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  // -------- Setters --------

  public void setId(UUID id) {
    this.id = id;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public void setRadio(Radio radio) {
    this.radio = radio;
  }

  public void setAssigneePerson(Person assigneePerson) {
    this.assigneePerson = assigneePerson;
  }

  public void setAssigneeWorkgroup(Workgroup assigneeWorkgroup) {
    this.assigneeWorkgroup = assigneeWorkgroup;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public void setStartAt(OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public void setExpectedEnd(OffsetDateTime expectedEnd) {
    this.expectedEnd = expectedEnd;
  }

  public void setEndAt(OffsetDateTime endAt) {
    this.endAt = endAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
