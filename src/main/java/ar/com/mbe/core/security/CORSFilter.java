package ar.com.mbe.core.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CORSFilter implements Filter {
    @Value("${security.cors_allow_origin}")
    private String corsAllowOrigin;

    @Value("${security.cors_allow_creden}")
    private String corsAllowCreden;

    @Value("${security.cors_allow_method}")
    private String corsAllowMethod;

    @Value("${security.cors_allow_header}")
    private String corsAllowHeader;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", corsAllowOrigin);
        response.setHeader("Access-Control-Allow-Credentials", corsAllowCreden);
        response.setHeader("Access-Control-Allow-Methods", corsAllowMethod);
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", corsAllowHeader);

        chain.doFilter(req, res);
    }
}
