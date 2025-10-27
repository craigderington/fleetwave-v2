package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
    name = "memberships",
    uniqueConstraints = {
      @UniqueConstraint(name = "uq_membership_person_group", columnNames = {"person_id", "workgroup_id"})
    })
public class Membership {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "tenant_id", nullable = false, updatable = false, length = 64)
  private String tenantId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "person_id", nullable = false)
  private Person person;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "workgroup_id", nullable = false)
  private Workgroup workgroup;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 16)
  private Role role;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  public enum Role {
    MEMBER,
    MANAGER
  }

  public Membership() {}

  public static Membership createNew(String tenantId, Person person, Workgroup workgroup, Role role) {
    Membership m = new Membership();
    m.id = UUID.randomUUID();
    m.tenantId = tenantId;
    m.person = person;
    m.workgroup = workgroup;
    m.role = role == null ? Role.MEMBER : role;
    m.createdAt = OffsetDateTime.now();
    return m;
  }

  public UUID getId() {
    return id;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public Workgroup getWorkgroup() {
    return workgroup;
  }

  public void setWorkgroup(Workgroup workgroup) {
    this.workgroup = workgroup;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Membership)) return false;
    Membership that = (Membership) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
