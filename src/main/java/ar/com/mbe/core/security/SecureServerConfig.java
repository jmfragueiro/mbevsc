package ar.com.mbe.core.security;

import ar.com.mbe.core.auth.AuthenticationFilter;
import ar.com.mbe.core.auth.IAuthenticator;
import ar.com.mbe.core.common.C;
import ar.com.mbe.core.handlers.AccessErrorHandler;
import ar.com.mbe.core.handlers.AuthenticationErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static java.util.Collections.singletonList;

@Configuration
@EnableWebSecurity//(debug = true)
public class SecureServerConfig {
    @Value("${security.cors_allow_origin}")
    private String corsAllowOrigin;

    @Value("${security.cors_allow_method}")
    private String corsAllowMethod;

    @Value("${security.cors_allow_header}")
    private String corsAllowHeader;

    @Autowired
    private AuthenticationErrorHandler authenticationErrorHandler;

    @Autowired
    private AccessErrorHandler accessErrorHandler;

    @Autowired
    private IAuthenticator authr;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           @Value("${security.paths.public}") String pubpaths) throws Exception {
        var pp = Arrays.stream(pubpaths.split(","))
                       .map(AntPathRequestMatcher::new)
                       .toArray(RequestMatcher[]::new);

        http
            // deshabilita el control de CSRF (ver en producción)
            .csrf(CsrfConfigurer::disable)
            // que no cachee el contexto de seguridad entre llamadas
            .securityContext(AbstractHttpConfigurer::disable)
            // deshabilita el request cache
            .requestCache(RequestCacheConfigurer::disable)
            // establece configurador de CORS
            .cors(csrf -> csrf.configurationSource(corsConfigurationSource()))
            // no se permiten requests anonimos
            .anonymous(AnonymousConfigurer::disable)
            // no utiliza, por ello no genera, el default login form
            .formLogin(FormLoginConfigurer::disable)
            // no utiliza, por ello no genera, default logout
            .logout(LogoutConfigurer::disable)
            // reemplaza el fitro de autenticación por el nuestro
            .addFilterAt(new AuthenticationFilter(authr, pp), UsernamePasswordAuthenticationFilter.class)
            // establece el gestor de error de autenticación
            .exceptionHandling(exhand -> exhand.authenticationEntryPoint(authenticationErrorHandler))
            // establece el gestor de error de autorización de acceso
            .exceptionHandling(exhand -> exhand.accessDeniedHandler(accessErrorHandler))
            // deshabilita la gestion de sesiones
            .sessionManagement(AbstractHttpConfigurer::disable)
            // establece los paths publicos y el resto debe ser autenticado
            .authorizeHttpRequests((authorize) -> authorize
                                                   .requestMatchers(pp)
                                                   .permitAll()
                                                   .anyRequest()
                                                   .authenticated());

        return http.build();
    }

    protected CorsConfigurationSource corsConfigurationSource() {
        // setea la configuración de seguridad de CORS
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(singletonList(corsAllowOrigin));
        configuration.setAllowedMethods(Arrays.stream(corsAllowMethod.split(",")).toList());
        configuration.setAllowedHeaders(Arrays.stream(corsAllowHeader.split(",")).toList());
        configuration.setAllowCredentials(true);

        // define para qué URLs aplicar esa configuración
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(C.SYS_CAD_URLALL, configuration);

        return source;
    }
}
