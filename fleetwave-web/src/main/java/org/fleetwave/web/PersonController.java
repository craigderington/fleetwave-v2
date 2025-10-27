package org.fleetwave.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.fleetwave.domain.Person;
import org.fleetwave.domain.repo.PersonRepository;
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
    public ResponseEntity<List<Person>> list() {
        return ResponseEntity.ok(people.findAll());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> create(@RequestHeader("X-Tenant") String tenantId,
                                         @RequestBody Person body) {
        if (body.getId() == null) body.setId(UUID.randomUUID());
        body.setTenantId(tenantId);
        if (body.getCreatedAt() == null) body.setCreatedAt(OffsetDateTime.now());
        body.setUpdatedAt(OffsetDateTime.now());
        return ResponseEntity.ok(people.save(body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        people.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
