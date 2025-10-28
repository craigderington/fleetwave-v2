package org.fleetwave.web.api.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class RequestDtos {

  public static class CreateRequest {
    @NotNull(message = "workgroupId is required")
    private UUID workgroupId;

    private UUID requesterId;
    private String modelPref;
    private String reason;

    public CreateRequest() {}

    public UUID getRequesterId() {
      return requesterId;
    }

    public void setRequesterId(UUID requesterId) {
      this.requesterId = requesterId;
    }

    public UUID getWorkgroupId() {
      return workgroupId;
    }

    public void setWorkgroupId(UUID workgroupId) {
      this.workgroupId = workgroupId;
    }

    public String getModelPref() {
      return modelPref;
    }

    public void setModelPref(String modelPref) {
      this.modelPref = modelPref;
    }

    public String getReason() {
      return reason;
    }

    public void setReason(String reason) {
      this.reason = reason;
    }
  }
}
