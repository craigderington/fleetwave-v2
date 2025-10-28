package org.fleetwave.web.api.dto;
import java.time.OffsetDateTime;
import java.util.UUID;

public class AssignmentDtos{

  public static class CreateAssignment{

    public UUID radioId;
    public String tenantId;
    public String assigneeType;
    public UUID assigneeId;
    public OffsetDateTime expectedEnd;
  }
}
