package ar.com.mbe.core.auth;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.H;
import ar.com.mbe.core.exception.AuthException;
import ar.com.mbe.core.extradata.ReqScopeExtraData;
import ar.com.mbe.core.jws.IJwsDataHelper;
import ar.com.mbe.core.jws.JwsService;
import ar.com.mbe.core.security.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class Authenticator implements IAuthenticator {
    @Autowired
    private IAuthenticationService authService;

    @Autowired
    private JwsService jwsService;

    @Autowired
    private ReqScopeExtraData reqScopeExtraData;

    @Value("${security.jwt.client-id}")
    private String clientid;

    @Value("${security.jwt.client-secret}")
    private String clientsecret;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthException {
        return authService.authenticate(authentication);
    }

    @Override
    public Authentication makeUnauthFrom(String jws) {
        return authService.construct(jwsService.getIdFromJws(jws));
    }

    @Override
    public Authentication makeUnauthFrom(String username, String password) {
        return authService.construct(username, password);
    }

    @Override
    public H login(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            throw new AuthException(C.MSJ_SEC_ERR_USERNOAUTH);
        }

        authService.login(authentication);
        SecurityService.setContextAuthentication(authentication);

        var data = (IJwsDataHelper) authentication.getPrincipal();

        return new H(LocalDateTime.now().toString(),
                     HttpStatus.CREATED.value(),
                     null,
                     jwsService.generateJws(data),
                     C.MSJ_SES_INF_LOGON,
                     C.SYS_CAD_LOGINURL,
                     EHttpReqAuthType.BEARER);
    }

    @Override
    public H logout(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            throw new AuthException(C.MSJ_SEC_ERR_USERNOAUTH);
        }

        SecurityService.clearContextAuthentication();
        authService.logout(authentication);

        return new H(LocalDateTime.now().toString(),
                     HttpStatus.OK.value(),
                     null,
                     authentication.getDetails().toString(),
                     C.MSJ_SES_INF_LOGOFF,
                     C.SYS_CAD_LOGOUTURL,
                     EHttpReqAuthType.BEARER);

    }

    @Override
    public String validateBearerCadFromRequest(HttpServletRequest request) {
        return this.validateBearerAuthCad(this.preValidateAutCadAndSplit(request, EHttpReqAuthType.BEARER));
    }

    @Override
    public void validateBasicCadFromRequest(HttpServletRequest request) {
        this.validateBasicAuthCad(this.preValidateAutCadAndSplit(request, EHttpReqAuthType.BASIC));
    }

    @Override
    public void setAuthExtraDataFromRequest(HttpServletRequest request) {
        reqScopeExtraData.set(
                jwsService.getExtraDataFromJws(this.preValidateAutCadAndSplit(request, EHttpReqAuthType.BEARER)));
    }

    private String preValidateAutCadAndSplit(HttpServletRequest request, EHttpReqAuthType type) {
        String reqauth = request.getHeader(C.SYS_APP_LOGIN_HTTP_AUTH);
        if (reqauth == null) {
            throw new AuthException(C.MSJ_SEC_ERR_BADREQUEST);
        }

        var split = reqauth.split(C.SYS_CAD_SPACE);
        if (split.length < 2) {
            throw new AuthException(C.MSJ_SEC_ERR_BADREQUEST);
        }

        if (!split[0].equalsIgnoreCase(type.name())) {
            throw new AuthException(C.MSJ_SEC_ERR_BADREQUESTVALUE);
        }

        String authcad = split[1].trim();
        if (authcad.isBlank()) {
            throw new AuthException(C.MSJ_SEC_ERR_BADREQUEST);
        }

        return authcad;
    }

    private void validateBasicAuthCad(String authcad) {
        ///////////////////////////////////////////////////////////////////
        // PARA VER/GENERAR CUAL ES EL CODIGO QUE DEBE UTILIZARSE:       //
        // var coded = new String(                                       //
        // 					Base64.getEncoder()                          //
        // 			  			  .encode(clientid.concat(":")           //
        // 			 				 			  .concat(clientsecret)  //
        // 			 				 			  .getBytes()));         //
        ///////////////////////////////////////////////////////////////////
        String authUser = new String(Base64.getDecoder().decode(authcad)).split(":")[0];
        String authSecret = new String(Base64.getDecoder().decode(authcad)).split(":")[1];
        if (!(authUser.equals(clientid) && authSecret.equals(clientsecret))) {
            throw new AuthException(C.MSJ_SEC_ERR_BADREQUESTVALUE);
        }
    }

    private String validateBearerAuthCad(String authcad) {
        return jwsService.validateJws(authcad);
    }
}
