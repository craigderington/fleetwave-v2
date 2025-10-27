package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.fleetwave.domain.base.TenantScoped;

@Entity
@Table(name = "assignments")
public class Assignment extends TenantScoped {

    public enum Status {
        REQUESTED,
        APPROVED,
        ASSIGNED,
        RETURNED,
        CANCELLED
    }

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radio_id", nullable = false)
    private Radio radio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_person_id")
    private Person assigneePerson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_workgroup_id")
    private Workgroup assigneeWorkgroup;

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(name = "expected_end")
    private OffsetDateTime expectedEnd;

    @Column(name = "end_at")
    private OffsetDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public Assignment() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Radio getRadio() { return radio; }
    public void setRadio(Radio radio) { this.radio = radio; }

    public Person getAssigneePerson() { return assigneePerson; }
    public void setAssigneePerson(Person assigneePerson) { this.assigneePerson = assigneePerson; }

    public Workgroup getAssigneeWorkgroup() { return assigneeWorkgroup; }
    public void setAssigneeWorkgroup(Workgroup assigneeWorkgroup) { this.assigneeWorkgroup = assigneeWorkgroup; }

    public OffsetDateTime getStartAt() { return startAt; }
    public void setStartAt(OffsetDateTime startAt) { this.startAt = startAt; }

    public OffsetDateTime getExpectedEnd() { return expectedEnd; }
    public void setExpectedEnd(OffsetDateTime expectedEnd) { this.expectedEnd = expectedEnd; }

    public OffsetDateTime getEndAt() { return endAt; }
    public void setEndAt(OffsetDateTime endAt) { this.endAt = endAt; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
