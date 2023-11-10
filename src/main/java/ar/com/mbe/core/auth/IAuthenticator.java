package ar.com.mbe.core.auth;

import ar.com.mbe.core.common.H;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

/**
 * Esta interface establece el comportamiento necesario, dentro de este "proto-framework",
 * para dar soporte a las operaciones de autenticación, con el mecanismo de Auntenticación
 * de springboot, y estableciendo algunos metodos que permitan independizar la capa cliente
 * (filtros, controladores y servicios) de su implementación efectiva.
 *
 * @author jmfragueiro
 * @version 20230601
 */
public interface IAuthenticator extends AuthenticationManager {
    Authentication makeUnauthFrom(String jws);

    Authentication makeUnauthFrom(String username, String password);

    H login(Authentication authentication);

    H logout(Authentication authentication);

    String validateBearerCadFromRequest(HttpServletRequest request);

    void validateBasicCadFromRequest(HttpServletRequest request);

    void setAuthExtraDataFromRequest(HttpServletRequest request);
}
