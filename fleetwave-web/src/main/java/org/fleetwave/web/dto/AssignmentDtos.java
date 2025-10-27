package org.fleetwave.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Container for assignment-related DTOs.
 */
public final class AssignmentDtos {

  private AssignmentDtos() { }

  public static final class AssignToPersonRequest {
    @NotNull
    private UUID radioId;

    @NotNull
    private UUID personId;

    // Optional; if null, service will set a default policy (e.g., +8h).
    private OffsetDateTime expectedEnd;

    public AssignToPersonRequest() { }

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

    public OffsetDateTime getExpectedEnd() {
      return expectedEnd;
    }

    public void setExpectedEnd(OffsetDateTime expectedEnd) {
      this.expectedEnd = expectedEnd;
    }
  }
}
