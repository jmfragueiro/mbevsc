package ar.com.mbe.refimpl;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.token.ITokenPayload;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public record RDefaultUser(
        Long id,
        String username,
        String password,
        Boolean locked,
        Boolean enabled,
        Boolean expired) implements ITokenPayload {
    private static Collection<GrantedAuthority> auths;

    public RDefaultUser {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        Objects.requireNonNull(locked);
        Objects.requireNonNull(enabled);
        Objects.requireNonNull(expired);

        auths = new HashSet<>();
        auths.add(new SimpleGrantedAuthority(C.SYS_APP_ROLE_ADMIN));
        auths.add(new SimpleGrantedAuthority(C.SYS_APP_ROLE_BASIC));
        auths.add(new SimpleGrantedAuthority(C.SYS_APP_ROLE_USER));
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public String getCredential() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isNonLocked() {
        return !locked;
    }

    @Override
    public boolean isNonExpired() {
        return !expired;
    }

    @Override
    public Collection<? extends GrantedAuthority> reinitAuthorities() {
        return auths;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return auths;
    }
}
