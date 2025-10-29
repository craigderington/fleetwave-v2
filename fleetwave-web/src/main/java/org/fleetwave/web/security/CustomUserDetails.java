package org.fleetwave.web.security;

import org.fleetwave.domain.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security UserDetails implementation wrapping a Person entity.
 * Provides authentication and authorization information.
 */
public class CustomUserDetails implements UserDetails {

    private final Person person;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Person person) {
        this.person = person;
        this.authorities = parseAuthorities(person.getRoles());
    }

    /**
     * Parse roles from comma-separated string into GrantedAuthority collection.
     * Example: "ROLE_USER,ROLE_ADMIN" -> [ROLE_USER, ROLE_ADMIN]
     */
    private Collection<? extends GrantedAuthority> parseAuthorities(String roles) {
        if (roles == null || roles.isBlank()) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return Arrays.stream(roles.split(","))
            .map(String::trim)
            .filter(role -> !role.isEmpty())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return person.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return person.getEnabled() != null && person.getEnabled();
    }

    // Expose Person entity for application use
    public Person getPerson() {
        return person;
    }

    public String getTenantId() {
        return person.getTenantId();
    }
}
