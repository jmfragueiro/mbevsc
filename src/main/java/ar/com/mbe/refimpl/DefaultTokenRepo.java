package ar.com.mbe.refimpl;

import ar.com.mbe.core.token.AbstractTokenRepo;
import ar.com.mbe.core.token.ITokenPayload;
import ar.com.mbe.core.token.Token;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

@Component
public class DefaultTokenRepo extends AbstractTokenRepo<String, ITokenPayload, Token> {
    @Override
    protected Map<String, Token> getRepo() {
        ////////////////////////////////////////////////////////////////////
        // se usa Hashtable para asegurar que no existan tokens sin clave //
        ////////////////////////////////////////////////////////////////////
        return new Hashtable<>();
    }
}
