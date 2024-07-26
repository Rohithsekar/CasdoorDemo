package com.casdoor.demo.security;


import org.casbin.casdoor.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(authentication -> {
                    // Check if the principal is a User object
                    if (authentication.getPrincipal() instanceof User) {
                        User user = (User) authentication.getPrincipal();
                        // Return the name field from the User object
                        return user.name;
                    } else {
                        // Handle the case where the principal is not a User
                        return null;
                    }
                });
    }
}
