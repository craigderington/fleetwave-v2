package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "workgroups")
public class Workgroup {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "tenant_id", nullable = false, length = 100)
  private String tenantId;

  @Column(name = "name", nullable = false, length = 200)
  private String name;

  @Column(name = "description")
  private String description;

  @ManyToMany
  @JoinTable(
      name = "workgroup_members",
      joinColumns = @JoinColumn(name = "workgroup_id"),
      inverseJoinColumns = @JoinColumn(name = "person_id"))
  private Set<Person> members = new HashSet<>();

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  public Workgroup() {}

  // --- getters ---

  public UUID getId() { return id; }
  public String getTenantId() { return tenantId; }
  public String getName() { return name; }
  public String getDescription() { return description; }
  public Set<Person> getMembers() { return members; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public OffsetDateTime getUpdatedAt() { return updatedAt; }

  // --- setters ---

  public void setId(UUID id) { this.id = id; }
  public void setTenantId(String tenantId) { this.tenantId = tenantId; }
  public void setName(String name) { this.name = name; }
  public void setDescription(String description) { this.description = description; }
  public void setMembers(Set<Person> members) { this.members = (members != null) ? members : new HashSet<>(); }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
  public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

  // --- convenience helpers (optional) ---

  public void addMember(Person p) {
    if (this.members == null) this.members = new HashSet<>();
    this.members.add(p);
  }

  public void removeMember(UUID personId) {
    if (this.members != null) {
      this.members.removeIf(x -> x != null && personId.equals(x.getId()));
    }
  }
}

