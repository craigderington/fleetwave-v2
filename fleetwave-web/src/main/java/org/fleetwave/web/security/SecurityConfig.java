package org.fleetwave.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for FleetWave.
 * Configures:
 * - Form-based authentication with Thymeleaf login page
 * - Session-based security (cookies)
 * - Role-based access control (ROLE_USER, ROLE_MANAGER, ROLE_ADMIN)
 * - Tenant filter integration
 * - Remember-me functionality
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private TenantFilter tenantFilter;

    /**
     * Password encoder using BCrypt with strength 10.
     * Matches the password hashes in the database migration.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Authentication provider that uses our custom UserDetailsService.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Authentication manager for programmatic authentication.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Custom success handler that stores tenant ID in session after login.
     */
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            // Store tenant ID in session for web requests
            if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
                request.getSession().setAttribute("tenantId", userDetails.getTenantId());
                request.getSession().setAttribute("username", userDetails.getUsername());
            }

            // Redirect based on role
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/admin/");
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
                response.sendRedirect("/portal/queue");
            } else {
                response.sendRedirect("/portal/");
            }
        };
    }

    /**
     * Main security filter chain configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Add tenant filter before authentication
            .addFilterBefore(tenantFilter, UsernamePasswordAuthenticationFilter.class)

            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (no authentication required)
                .requestMatchers(
                    "/",
                    "/login",
                    "/logout",
                    "/error",
                    "/status",
                    "/webjars/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/favicon.ico"
                ).permitAll()

                // Swagger/API docs (permitAll for now, can be restricted later)
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/actuator/**"
                ).permitAll()

                // Admin-only endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Manager and Admin can access approval queue
                .requestMatchers("/portal/queue/**").hasAnyRole("MANAGER", "ADMIN")

                // Portal accessible to all authenticated users
                .requestMatchers("/portal/**").authenticated()

                // API endpoints require authentication
                .requestMatchers("/api/**").authenticated()

                // All other requests require authentication
                .anyRequest().authenticated()
            )

            // Form-based login configuration
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler())
                .failureUrl("/login?error")
                .permitAll()
            )

            // Logout configuration
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            // Remember-me functionality (14 days)
            .rememberMe(remember -> remember
                .key("fleetwave-remember-me-key")
                .tokenValiditySeconds(1209600) // 14 days
                .userDetailsService(userDetailsService)
            )

            // Session management
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )

            // CSRF protection (enabled by default for form-based apps)
            .csrf(csrf -> csrf
                // Disable CSRF for API endpoints (use token-based auth instead)
                .ignoringRequestMatchers("/api/**")
            );

        return http.build();
    }
}
