package org.fleetwave.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.fleetwave.domain.Person;
import org.fleetwave.domain.repo.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/people", produces = APPLICATION_JSON_VALUE)
public class PersonController {

  private final PersonRepository people;

  public PersonController(PersonRepository people) {
    this.people = people;
  }

  @GetMapping
  public ResponseEntity<Page<Person>> list(
      @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {
    return ResponseEntity.ok(people.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Person> getById(@PathVariable UUID id) {
    Person person =
        people.findById(id).orElseThrow(() -> new IllegalArgumentException("Person not found"));
    return ResponseEntity.ok(person);
  }

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<Person> create(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @RequestBody Person body) {
    if (body.getId() == null) body.setId(UUID.randomUUID());
    body.setTenantId(tenantId);
    if (body.getCreatedAt() == null) body.setCreatedAt(OffsetDateTime.now());
    body.setUpdatedAt(OffsetDateTime.now());
    return ResponseEntity.status(HttpStatus.CREATED).body(people.save(body));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Person> update(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable UUID id,
      @RequestBody Person body) {
    Person existing =
        people
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Person not found"));

    if (body.getFirstName() != null) existing.setFirstName(body.getFirstName());
    if (body.getLastName() != null) existing.setLastName(body.getLastName());
    if (body.getEmail() != null) existing.setEmail(body.getEmail());
    existing.setUpdatedAt(OffsetDateTime.now());

    return ResponseEntity.ok(people.save(existing));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @RequestHeader("X-Tenant") @NotBlank(message = "X-Tenant header is required")
          String tenantId,
      @PathVariable("id") UUID id) {
    Person existing =
        people
            .findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Person not found"));
    people.delete(existing);
    return ResponseEntity.noContent().build();
  }
}
