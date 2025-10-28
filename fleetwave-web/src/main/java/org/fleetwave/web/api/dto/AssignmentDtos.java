package org.fleetwave.web.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

public class AssignmentDtos {

  public static class CreateAssignment {
    @NotNull(message = "radioId is required")
    private UUID radioId;

    @NotNull(message = "assigneeId is required")
    private UUID assigneeId;

    private String assigneeType;
    private OffsetDateTime expectedEnd;

    public CreateAssignment() {}

    public UUID getRadioId() {
      return radioId;
    }

    public void setRadioId(UUID radioId) {
      this.radioId = radioId;
    }

    public UUID getAssigneeId() {
      return assigneeId;
    }

    public void setAssigneeId(UUID assigneeId) {
      this.assigneeId = assigneeId;
    }

    public String getAssigneeType() {
      return assigneeType;
    }

    public void setAssigneeType(String assigneeType) {
      this.assigneeType = assigneeType;
    }

    public OffsetDateTime getExpectedEnd() {
      return expectedEnd;
    }

    public void setExpectedEnd(OffsetDateTime expectedEnd) {
      this.expectedEnd = expectedEnd;
    }
  }
}
