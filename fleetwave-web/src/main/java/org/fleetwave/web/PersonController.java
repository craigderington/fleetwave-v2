package org.fleetwave.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.fleetwave.domain.Person;
import org.fleetwave.domain.repo.PersonRepository;
import org.fleetwave.web.dto.PersonDtos.CreatePersonRequest;
import org.fleetwave.web.dto.PersonDtos.UpdatePersonRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
@Tag(name = "Persons")
public class PersonController {

  private final PersonRepository personRepo;

  public PersonController(PersonRepository personRepo) {
    this.personRepo = personRepo;
  }

  @GetMapping
  public List<Person> list(@RequestHeader("X-Tenant") String tenantId) {
    return personRepo.findAllByTenantId(tenantId);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Person> get(
      @RequestHeader("X-Tenant") String tenantId, @PathVariable("id") UUID id) {
    return personRepo.findByIdAndTenantId(id, tenantId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @Transactional
  public ResponseEntity<Person> create(
      @RequestHeader("X-Tenant") String tenantId, @RequestBody CreatePersonRequest req) {

    if (req.firstName == null || req.lastName == null) {
      return ResponseEntity.badRequest().build();
    }

    Person p =
        Person.createNew(
            tenantId, req.firstName, req.lastName, req.email, req.phone, req.externalRef);

    return ResponseEntity.ok(personRepo.save(p));
  }

  @PutMapping("/{id}")
  @Transactional
  public ResponseEntity<Person> update(
      @RequestHeader("X-Tenant") String tenantId,
      @PathVariable("id") UUID id,
      @RequestBody UpdatePersonRequest req) {

    return personRepo
        .findByIdAndTenantId(id, tenantId)
        .map(
            p -> {
              if (req.firstName != null) p.setFirstName(req.firstName);
              if (req.lastName != null) p.setLastName(req.lastName);
              if (req.email != null) p.setEmail(req.email);
              if (req.phone != null) p.setPhone(req.phone);
              if (req.externalRef != null) p.setExternalRef(req.externalRef);
              if (req.status != null) {
                p.setStatus(Person.Status.valueOf(req.status));
              }
              return ResponseEntity.ok(personRepo.save(p));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<Void> delete(
      @RequestHeader("X-Tenant") String tenantId, @PathVariable("id") UUID id) {

    return personRepo
        .findByIdAndTenantId(id, tenantId)
        .map(
            p -> {
              personRepo.delete(p);
              return ResponseEntity.<Void>noContent().build();
            })
        .orElseGet(() -> ResponseEntity.<Void>notFound().build());
  }
}
