package org.fleetwave.web.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class AssignmentDtos {

  private AssignmentDtos() {}

  public static class ReturnRequest {
    public UUID assignmentId;
    public String returnedAt; // ISO-8601 string, optional
  }
}
