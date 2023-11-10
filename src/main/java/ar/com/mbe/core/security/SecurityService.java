package ar.com.mbe.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static boolean passwordsMatch(String pass1, String pass2) {
        return SecurityService.passwordEncoder.matches(pass1, pass2);
    }

    public static String encodePassword(CharSequence rawpass) {
        return SecurityService.passwordEncoder.encode(rawpass);
    }

    public static void setContextAuthentication(Authentication auth) {
        clearContextAuthentication();
        SecurityContext context = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.getContextHolderStrategy().setContext(context);
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void clearContextAuthentication() {
        SecurityContextHolder.getContextHolderStrategy().clearContext();
    }
}
