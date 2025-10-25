package org.fleetwave.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/login", "/css/**", "/js/**", "/portal/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
        .anyRequest().authenticated()
      )
      .formLogin(Customizer.withDefaults())
      .logout(Customizer.withDefaults())
      .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
    return http.build();
  }
}
