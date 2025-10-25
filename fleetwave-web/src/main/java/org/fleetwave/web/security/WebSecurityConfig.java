package org.fleetwave.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain appChain(HttpSecurity http) throws Exception {
    http
      .cors(c -> c.configurationSource(corsSource()))
      .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
      .headers(h -> h
        .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).preload(true).maxAgeInSeconds(31536000))
        .contentTypeOptions(Customizer.withDefaults())
        .xssProtection(Customizer.withDefaults())
        .referrerPolicy(r -> r.policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
      );
    return http.build();
  }

  private UrlBasedCorsConfigurationSource corsSource(){
    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowedOrigins(List.of("https://portal.fleetwave.org"));
    cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
    cfg.setAllowedHeaders(List.of("Content-Type","Authorization","Idempotency-Key"));
    cfg.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
    src.registerCorsConfiguration("/api/**", cfg);
    return src;
  }
}
