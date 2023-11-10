package ar.com.mbe.core.token;

/**
 * Servicio para la gestión de ITOKENs en el presente "proto-framework". Este servicio se
 * apoya en un "repositorio de itokens" el cual debe ser inyectado de alguna manera y es
 * quien realmente implementa el comportamiento de resguardo de estos. En la implenentación
 * efectiva aquí se da flexibilidad al soportar la inyección "parametrizada" del repositorio
 * a utilizarse (es decir permitiendo definir el servicio concreto vía application.properties
 * en la propiedad: security.token.defaultrepo).
 *
 * @author jmfragueiro
 * @version 20230601
 * @param <K> la clase de un identificador posible para el tipo de ITOKEN
 * @param <U> la clase del objeto (payload) a ser mantenido por el ITOKEN
 * @param <T> la clase que implementa el ITOKEN
 * @param <R> la clase que implementa el repositorio de tokens
 */
public interface ITokenService<K, U extends ITokenPayload, T extends IToken<K, U>, R extends ITokenRepo<K, U, T>> {
    T createToken(ITokenPayload payload);

    T initToken(T token);

    T getToken(K clave);

    boolean tokenExists(T token);

    void endToken(T token);

    R getRepo();
}
