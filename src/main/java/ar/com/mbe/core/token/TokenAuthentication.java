package ar.com.mbe.core.token;

import ar.com.mbe.core.common.C;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class TokenAuthentication implements ITokenAuthentication<String, ITokenPayload, IToken<String, ITokenPayload>> {
    private final String name;
    private String credential;
    private final IToken<String, ITokenPayload> principal;
    private final String details;
    private final boolean authenticated;

    public TokenAuthentication(String seed) {
        this.name = null;
        this.credential = null;
        this.principal = null;
        this.details = seed;
        this.authenticated = false;
    }

    public TokenAuthentication(String username, String password) {
        this.name = username;
        this.credential = password;
        this.principal = null;
        this.details = username;
        this.authenticated = false;
    }

    public TokenAuthentication(IToken<String, ITokenPayload> token, String details) {
        this.name = null;
        this.credential = null;
        this.principal = token;
        this.details = details;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (principal != null) ? principal.getPayload().getAuthorities() : Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return credential;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // nada solamente se toca desde los constructores
        throw new IllegalArgumentException(C.MSJ_SEC_ERR_TOKENCHANGESTATE);
    }

    @Override
    public String getName() {
        return (this.isAuthenticated() && this.principal != null
                ? this.principal.getPayload().getName()
                : this.name);
    }

    @Override
    public void eraseCredentials() {
        this.credential = null;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
