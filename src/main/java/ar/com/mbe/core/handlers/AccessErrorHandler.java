package ar.com.mbe.core.handlers;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.E;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccessErrorHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException aex)
            throws IOException {
        E.respondGenericError(response, HttpStatus.UNAUTHORIZED.value(), aex.getMessage(), C.MSJ_SEC_INF_NOACCES,
                              request.getServletPath());
    }
}
