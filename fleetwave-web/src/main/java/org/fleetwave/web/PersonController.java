package org.fleetwave.web;

import org.fleetwave.domain.Person;
import org.fleetwave.domain.repo.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/persons", produces = "application/json")
public class PersonController {

  private final PersonRepository personRepository;

  public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @RequestHeader("X-Tenant") String tenantId,
      @PathVariable UUID id) {

    Person p = personRepository.findByIdAndTenantId(id, tenantId)
        .orElseThrow(() -> new IllegalArgumentException("Person not found"));
    personRepository.delete(p);
    return ResponseEntity.ok().build();
  }
}
