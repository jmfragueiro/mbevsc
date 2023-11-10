package ar.com.mbe.core.token;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.exception.TokenException;
import ar.com.mbe.refimpl.DefaultTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService implements ITokenService<String, ITokenPayload, Token, ITokenRepo<String, ITokenPayload, Token>> {
    private final ITokenRepo<String, ITokenPayload, Token> tokenRepo;

    @Value("${security.token.validity}")
    private String validity;

    @Autowired
    public TokenService(List<ITokenRepo<String, ITokenPayload, Token>> tokenRepos,
                        @Value("${security.token.defaultrepo}") String defaultrepo) {
        this.tokenRepo = (tokenRepos != null) ? tokenRepos.stream()
                                                          .filter(e -> e.getClass().getName().equals(defaultrepo))
                                                          .findFirst()
                                                          .orElse(new DefaultTokenRepo()) : null;
    }

    @Override
    public Token createToken(ITokenPayload source) {
        return new Token(source);
    }

    @Override
    public final Token initToken(Token token) {
        if (!tokenExists(token)) {
            tokenRepo.addToken(token.getId(), token);
        }
        token.reinitTerm(Long.parseLong(validity));
        return token;
    }

    @Override
    public final Token getToken(String clave) {
        if (clave == null || clave.isBlank()) {
            throw new TokenException(C.MSJ_SES_ERR_NOTOKENVALUEINFO);
        }

        return tokenRepo.getToken(clave);
    }

    @Override
    public boolean tokenExists(Token token) {
        return tokenRepo.tokenExist(token.getId());
    }

    @Override
    public void endToken(Token token) {
        tokenRepo.removeToken(token.getId());
    }

    @Override
    public ITokenRepo<String, ITokenPayload, Token> getRepo() {
        return tokenRepo;
    }
}

