package org.fleetwave.web.security;

import org.fleetwave.domain.Person;
import org.fleetwave.domain.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService that loads Person entities from the database.
 * Used by Spring Security during authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get tenant ID from context (set by TenantFilter)
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new UsernameNotFoundException("No tenant context available");
        }

        // Find person by tenant and username
        Person person = personRepository.findByTenantIdAndUsername(tenantId, username)
            .orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found in tenant '%s'", username, tenantId)
            ));

        return new CustomUserDetails(person);
    }
}
