package ar.com.mbe.core.token;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.F;
import ar.com.mbe.core.common.T;
import ar.com.mbe.core.exception.AuthException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class Token extends AbstractToken<String, ITokenPayload> {
    public Token(ITokenPayload objeto) {
        super(objeto);
    }

    @Override
    public String getId() {
        // aqui se utiliza, como identificador, al nombre de usuario
        // mas el datetime de creación del token, para asi segurar la
        // ´sincronizacion´ es decir que luego no se permita utilizar
        // un token viejo del mismo usuario
        return T.getSha256Token(
                getPayload().getName()
                            .concat(C.SYS_CAD_CLOSEREF)
                            .concat(F.trasnformarLDTaString(getIssuedAt())));
    }

    @Override
    public IToken<String, ITokenPayload> getUseful() {
        getPayload().verifyCanOperate();
        return this;
    }

    @Override
    public List<String> authClaim() {
        try {
            return getPayload().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        } catch (Exception e) {
            throw new AuthException(C.MSJ_SEC_ERR_NOGRANTS, e.getMessage());
        }
    }

    @Override
    public String toString() {
        return getId();
    }
}
