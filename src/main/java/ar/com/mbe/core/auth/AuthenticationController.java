package ar.com.mbe.core.auth;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.T;
import ar.com.mbe.core.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(C.SYS_CAD_AUTHURL)
public class AuthenticationController {
    @Autowired
    private IAuthenticator authenticator;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(
                        authenticator.logout(
                            authenticator.authenticate(
                                authenticator.makeUnauthFrom(
                                        authenticator.validateBearerCadFromRequest(request)))));
        } catch (Exception e) {
            throw new AuthException(T.getCadenaErrorFormateada(C.MSJ_SES_ERR_LOGOFF, e.getMessage(), C.SYS_CAD_UNKNOW));
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> login(String username, String password, HttpServletRequest request) {
        try {
            authenticator.validateBasicCadFromRequest(request);
            return ResponseEntity.ok(
                        authenticator.login(
                            authenticator.authenticate(
                                authenticator.makeUnauthFrom(username, password))));
        } catch (Exception e) {
            throw new AuthException(T.getCadenaErrorFormateada(C.MSJ_SES_ERR_LOGIN, e.getMessage(), username));
        }
    }
}
