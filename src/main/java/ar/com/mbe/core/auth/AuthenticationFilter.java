package ar.com.mbe.core.auth;

import ar.com.mbe.core.security.SecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final RequestMatcher publicPaths;

    public AuthenticationFilter(IAuthenticator authenticationManager, RequestMatcher[] publicPaths) {
        super(authenticationManager);
        super.setPostOnly(false);
        this.publicPaths = new OrRequestMatcher(publicPaths);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var auth = (IAuthenticator) this.getAuthenticationManager();
        return auth.authenticate(
                    auth.makeUnauthFrom(
                        auth.validateBearerCadFromRequest(request)));
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return !(publicPaths.matches(request));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityService.setContextAuthentication(authResult);

        ////////////////////////////////////////////////////////////////////////
        // Desde aqui, si autenticó OK, se continua agregando los datos extras//
        // que vienen en el requerimiento (dentro del jws) y que pueden usarse//
        // mas adelante y se continua "hacia abajo" con la cadena de filtros  //
        ////////////////////////////////////////////////////////////////////////
        ((IAuthenticator)this.getAuthenticationManager()).setAuthExtraDataFromRequest(request);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityService.clearContextAuthentication();

        ////////////////////////////////////////////////////////////////////////
        // Desde aqui, si no autenticó OK, se para la cadena de filtros y se  //
        // vuelve al cliente con un error HTTP                                //
        ////////////////////////////////////////////////////////////////////////
        response.sendError(HttpStatus.UNAUTHORIZED.value(), failed.getMessage());
    }
}
