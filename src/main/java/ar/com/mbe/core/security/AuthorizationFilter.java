package ar.com.mbe.core.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class AuthorizationFilter implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        return new AuthorizationDecision(canAccess(authentication.get(), object.getRequest().getServletPath()));
    }

    private boolean canAccess(Authentication auth, String resource) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(resource));
    }
}
