package org.fleetwave.web.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class AssignmentDtos {
  private AssignmentDtos() {}

  public static final class AssignToPersonRequest {
    public UUID radioId;
    public UUID personId;
    public OffsetDateTime expectedEnd;
  }
}
