package org.fleetwave.web.controllers;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.repo.AssignmentRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@RestController @RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
public class AssignmentExportApi {
  private final AssignmentRepository repo;

  @GetMapping(value="/export.csv", produces = "text/csv")
  public ResponseEntity<byte[]> exportCsv(@RequestParam(required=false) String status){
    var list = repo.findAll(); // TODO: add filters; simplified here
    String csv = "id,radioId,assigneeType,assigneeId,startAt,expectedEnd,endAt,status\n" +
      list.stream().map(a -> String.join("," ,
        a.getId().toString(), a.getRadio().getId().toString(), a.getAssigneeType().name(),
        a.getAssigneeId().toString(), String.valueOf(a.getStartAt()), String.valueOf(a.getExpectedEnd()),
        String.valueOf(a.getEndAt()), a.getStatus().name())).collect(Collectors.joining("\n"));
    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=assignments.csv")
      .contentType(new MediaType("text","csv", StandardCharsets.UTF_8))
      .body(csv.getBytes(StandardCharsets.UTF_8));
  }
}
