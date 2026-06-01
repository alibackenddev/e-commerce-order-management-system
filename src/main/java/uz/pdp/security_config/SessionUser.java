package uz.pdp.security_config;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.pdp.service.CustomUserDetails;

import java.util.Objects;

@Component
public class SessionUser {

    public CustomUserDetails  userDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return ((CustomUserDetails) Objects.requireNonNull(securityContext.getAuthentication()).getPrincipal());
    }

    public String sessionUser() {
        return userDetails().getUsername();
    }
}
