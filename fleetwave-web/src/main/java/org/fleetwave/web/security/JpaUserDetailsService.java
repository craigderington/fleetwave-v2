package org.fleetwave.web.security;

import lombok.RequiredArgsConstructor;
import org.fleetwave.domain.repo.UserRepository;
import org.fleetwave.domain.repo.UserRoleRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
  private final UserRepository users;
  private final UserRoleRepository userRoles;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var u = users.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    var roles = userRoles.findByUser_Id(u.getId()).stream()
      .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getName()))
      .collect(Collectors.toSet());
    return new User(u.getEmail(), u.getPasswordHash(), u.isEnabled(), true, true, true, roles);
  }
}
