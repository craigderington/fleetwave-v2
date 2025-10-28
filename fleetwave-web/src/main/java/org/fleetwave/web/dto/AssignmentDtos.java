package org.fleetwave.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

public final class AssignmentDtos {

  private AssignmentDtos() {}

  public static class AssignToPersonRequest {
    @NotNull(message = "radioId is required")
    private UUID radioId;

    @NotNull(message = "personId is required")
    private UUID personId;

    private OffsetDateTime dueAt;

    public AssignToPersonRequest() {}

    public UUID getRadioId() {
      return radioId;
    }

    public void setRadioId(UUID radioId) {
      this.radioId = radioId;
    }

    public UUID getPersonId() {
      return personId;
    }

    public void setPersonId(UUID personId) {
      this.personId = personId;
    }

    public OffsetDateTime getDueAt() {
      return dueAt;
    }

    public void setDueAt(OffsetDateTime dueAt) {
      this.dueAt = dueAt;
    }
  }
}
