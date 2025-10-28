package org.fleetwave.web.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

public class WorkOrderDtos {

  public static class CreateWorkOrder {
    @NotNull(message = "radioId is required")
    private UUID radioId;

    @NotBlank(message = "title is required")
    private String title;

    private String description;
    private OffsetDateTime dueAt;
    private String createdBy;

    public CreateWorkOrder() {}

    public UUID getRadioId() {
      return radioId;
    }

    public void setRadioId(UUID radioId) {
      this.radioId = radioId;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public OffsetDateTime getDueAt() {
      return dueAt;
    }

    public void setDueAt(OffsetDateTime dueAt) {
      this.dueAt = dueAt;
    }

    public String getCreatedBy() {
      return createdBy;
    }

    public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
    }
  }
}
