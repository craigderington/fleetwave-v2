package org.fleetwave.web.controllers;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController @RequiredArgsConstructor
public class UserApi {
  private final UserRepository users;

  @GetMapping("/api/users/me")
  public Map<String,Object> me(Authentication auth){
    var u = users.findByEmail(auth.getName()).orElseThrow();
    return Map.of("id", u.getId(), "email", u.getEmail(), "name", u.getName());
  }
}
