package ar.com.mbe.core.token;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.L;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

public abstract class AbstractTokenRepo<K, U extends ITokenPayload, T extends IToken<K, U>> implements ITokenRepo<K, U, T> {
    private final Map<K, T> tokens = getRepo();

    @Value("${security.min_token_validity}")
    private Long min_token_validity;

    protected abstract Map<K, T> getRepo();

    @Override
    public void addToken(K clave, T token) {
        token.reinitTerm(min_token_validity);
        tokens.put(clave, token);
    }

    @Override
    public int size() {
        return tokens.size();
    }

    @Override
    public boolean tokenExist(K clave) {
        return tokens.containsKey(clave);
    }

    @Override
    public T getToken(K clave) {
        return tokens.get(clave);
    }

    @Override
    public void removeToken(K clave) {
        tokens.remove(clave);
        L.info(C.MSJ_SEC_INF_TOKEREPODEL);
    }

    @Override
    public List<T> getList() {
        return tokens.values().stream().toList();
    }
}
