package ar.com.mbe.core.token;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.exception.AuthException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Un TOKEN es un elemento que permite mantener "registrado" otro objeto cualquiera denominado
 * PAYLOAD o carga útil. Esta interface permite establecer el comportamiento de una clase para
 * esa carga útil registrada.
 *
 * @author jmfragueiro
 * @version 20230601
 */
public interface ITokenPayload {
    Long getId();

    String getName();

    String getCredential();

    Collection<? extends GrantedAuthority> reinitAuthorities();

    Collection<? extends GrantedAuthority> getAuthorities();

    boolean isNonExpired();

    boolean isNonLocked();

    boolean isEnabled();

    default void verifyCanOperate() {
        if (!isNonLocked()) {
            throw new AuthException(C.MSJ_SEC_ERR_USERLOCKED);
        }
        if (!isEnabled()) {
            throw new AuthException(C.MSJ_SEC_ERR_USERNOTENABLED);
        }
        if (!isNonExpired()) {
            throw new AuthException(C.MSJ_SEC_ERR_USEREXPIRED);
        }
    }
}
