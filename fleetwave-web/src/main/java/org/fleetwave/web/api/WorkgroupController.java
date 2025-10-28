package org.fleetwave.web.api;

import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.fleetwave.domain.Workgroup;
import org.fleetwave.domain.repo.WorkgroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workgroups")
public class WorkgroupController {
  private final WorkgroupRepository workgroups;

  public WorkgroupController(WorkgroupRepository workgroups) {
    this.workgroups = workgroups;
  }

  @GetMapping
  public ResponseEntity<Page<Workgroup>> list(
      @PageableDefault(size = 20, sort = "name") Pageable pageable) {
    return ResponseEntity.ok(workgroups.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Workgroup> getById(@PathVariable UUID id) {
    Workgroup workgroup =
        workgroups.findById(id).orElseThrow(() -> new IllegalArgumentException("Workgroup not found"));
    return ResponseEntity.ok(workgroup);
  }

  @PostMapping
  public ResponseEntity<Workgroup> create(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @RequestBody Workgroup body) {
    if (body.getId() == null) body.setId(UUID.randomUUID());
    body.setTenantId(tenantId);
    if (body.getCreatedAt() == null) body.setCreatedAt(OffsetDateTime.now());
    body.setUpdatedAt(OffsetDateTime.now());
    return ResponseEntity.status(HttpStatus.CREATED).body(workgroups.save(body));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Workgroup> update(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID id,
      @RequestBody Workgroup body) {
    Workgroup existing =
        workgroups
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Workgroup not found"));

    if (body.getName() != null) existing.setName(body.getName());
    existing.setUpdatedAt(OffsetDateTime.now());

    return ResponseEntity.ok(workgroups.save(existing));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID id) {
    Workgroup existing =
        workgroups
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Workgroup not found"));
    workgroups.delete(existing);
    return ResponseEntity.noContent().build();
  }
}
