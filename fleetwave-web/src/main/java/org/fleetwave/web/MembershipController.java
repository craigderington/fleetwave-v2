package org.fleetwave.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.UUID;
import org.fleetwave.domain.Person;
import org.fleetwave.domain.Workgroup;
import org.fleetwave.domain.repo.PersonRepository;
import org.fleetwave.domain.repo.WorkgroupRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/memberships", produces = APPLICATION_JSON_VALUE)
public class MembershipController {

    private final PersonRepository personRepo;
    private final WorkgroupRepository workgroupRepo;

    public MembershipController(PersonRepository personRepo, WorkgroupRepository workgroupRepo) {
        this.personRepo = personRepo;
        this.workgroupRepo = workgroupRepo;
    }

    @GetMapping("/workgroups/{id}")
    public ResponseEntity<Workgroup> getWorkgroup(@RequestHeader("X-Tenant") String tenantId,
                                                  @PathVariable("id") UUID id) {
        return workgroupRepo.findByIdAndTenantId(id, tenantId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/people")
    public ResponseEntity<List<Person>> getPeople() {
        return ResponseEntity.ok(personRepo.findAll());
    }

    @DeleteMapping("/workgroups/{id}")
    public ResponseEntity<Void> deleteWorkgroup(@PathVariable("id") UUID id) {
        workgroupRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
