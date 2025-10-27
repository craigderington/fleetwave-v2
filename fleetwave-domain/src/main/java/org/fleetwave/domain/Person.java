package org.fleetwave.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "persons")
public class Person {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "tenant_id", nullable = false, updatable = false, length = 64)
  private String tenantId;

  @Column(name = "external_ref", length = 128)
  private String externalRef;

  @Column(name = "first_name", nullable = false, length = 64)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 64)
  private String lastName;

  @Column(name = "email", length = 255)
  private String email;

  @Column(name = "phone", length = 32)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 16)
  private Status status;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  public enum Status {
    ACTIVE,
    INACTIVE
  }

  public Person() {
    // JPA
  }

  public static Person createNew(
      String tenantId,
      String firstName,
      String lastName,
      String email,
      String phone,
      String externalRef) {
    Person p = new Person();
    p.id = UUID.randomUUID();
    p.tenantId = tenantId;
    p.firstName = firstName;
    p.lastName = lastName;
    p.email = email;
    p.phone = phone;
    p.externalRef = externalRef;
    p.status = Status.ACTIVE;
    OffsetDateTime now = OffsetDateTime.now();
    p.createdAt = now;
    p.updatedAt = now;
    return p;
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

  public String getExternalRef() {
    return externalRef;
  }

  public void setExternalRef(String externalRef) {
    this.externalRef = externalRef;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @PreUpdate
  public void onUpdate() {
    this.updatedAt = OffsetDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Person)) return false;
    Person person = (Person) o;
    return Objects.equals(id, person.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
