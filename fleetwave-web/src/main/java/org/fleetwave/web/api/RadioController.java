package org.fleetwave.web.api;

import java.util.*;
import org.fleetwave.domain.*;
import org.fleetwave.domain.repo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/radios")
public class RadioController {
  private final RadioRepository radios;

  public RadioController(RadioRepository r) {
    radios = r;
  }

  @GetMapping
  public Page<Radio> list(@PageableDefault(size = 20, sort = "serial") Pageable pageable) {
    return radios.findAll(pageable);
  }

  @PostMapping
  public Radio create(@RequestBody Radio radio) {
    if (radio.getId() == null) radio.setId(java.util.UUID.randomUUID());
    return radios.save(radio);
  }
}
