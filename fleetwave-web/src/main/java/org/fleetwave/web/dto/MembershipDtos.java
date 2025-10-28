package org.fleetwave.web.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public final class MembershipDtos {
  private MembershipDtos() {}

  public static final class AddMembershipRequest {
    @NotNull(message = "personId is required")
    private UUID personId;

    @NotNull(message = "workgroupId is required")
    private UUID workgroupId;

    @NotNull(message = "role is required")
    private String role; // MEMBER / MANAGER

    public AddMembershipRequest() {}

    public UUID getPersonId() {
      return personId;
    }

    public void setPersonId(UUID personId) {
      this.personId = personId;
    }

    public UUID getWorkgroupId() {
      return workgroupId;
    }

    public void setWorkgroupId(UUID workgroupId) {
      this.workgroupId = workgroupId;
    }

    public String getRole() {
      return role;
    }

    public void setRole(String role) {
      this.role = role;
    }
  }
}
