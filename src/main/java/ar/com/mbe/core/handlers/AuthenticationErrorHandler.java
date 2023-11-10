package ar.com.mbe.core.handlers;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.E;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationErrorHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException aex)
            throws IOException {
        E.respondGenericError(response, HttpStatus.UNAUTHORIZED.value(), aex.getMessage(), C.MSJ_SES_ERR_BADREQAUTH,
                              request.getServletPath());
    }
}
