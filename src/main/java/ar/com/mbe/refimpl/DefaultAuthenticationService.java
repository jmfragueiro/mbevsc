package ar.com.mbe.refimpl;

import ar.com.mbe.core.auth.IAuthenticationService;
import ar.com.mbe.core.common.C;
import ar.com.mbe.core.exception.AuthException;
import ar.com.mbe.core.security.SecurityService;
import ar.com.mbe.core.token.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultAuthenticationService implements IAuthenticationService {
    private final ITokenPayloadService<? extends ITokenPayload> payloadService;

    private final TokenService tokenService;

    @Autowired
    public DefaultAuthenticationService(TokenService tokenService,
                                        List<ITokenPayloadService<? extends ITokenPayload>> payloadServices,
                                        @Value("${security.payload.service}") String srvname) {
        this.tokenService = tokenService;
        this.payloadService = payloadServices.stream()
                                            .filter(e -> e.getClass().getName().equals(srvname))
                                            .findFirst()
                                            .orElseThrow(() -> new AuthException(C.MSJ_SEC_ERR_NOUSERSERVICE, srvname));
    }

    @Override
    public Authentication construct(String seed) {
        return new TokenAuthentication(seed);

    }

    @Override
    public Authentication construct(String username, String password) {
        return new TokenAuthentication(username, password);

    }

    @Override
    public Authentication authenticate(Authentication auth) {
        return (auth.getCredentials() != null ? autenticateFromRequest(auth) : autenticateFromRepo(auth));
    }

    @Override
    public void login(Authentication auth) {
        tokenService.initToken((Token) auth.getPrincipal());
    }

    @Override
    public void logout(Authentication auth) {
        tokenService.endToken((Token) auth.getPrincipal());
    }

    private Authentication autenticateFromRequest(Authentication auth) {
        var source = payloadService.findByName(auth.getName())
                                   .orElseThrow(() -> new AuthException(C.MSJ_SES_INF_BADCREDENTIAL));

        if (!SecurityService.passwordsMatch(auth.getCredentials().toString(), source.getCredential())) {
            throw new AuthException(C.MSJ_SES_INF_BADCREDENTIAL);
        }

        return this.getAuthenticatedToken(tokenService.createToken(source), auth.getDetails().toString());
    }

    private Authentication autenticateFromRepo(Authentication auth) {
        var token = tokenService.getToken(auth.getDetails().toString());

        if (token == null) {
            throw new AuthException(C.MSJ_SEC_ERR_NOTOKEN);
        }

        return this.getAuthenticatedToken(token, auth.getDetails().toString());
    }

    private TokenAuthentication getAuthenticatedToken(IToken<String, ITokenPayload> token, String details) {
        return new TokenAuthentication(token.getUseful(), details);
    }
}
