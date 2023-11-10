package ar.com.mbe.core.token;

import java.util.List;

/**
 * Esta interfase representa el comportamiento esperado de un repositorio
 * de TOKENs (IToken), permitiendo, además de los mensajes propios de un
 * repositorio, "reiniciar" (reiniciar su vencimiento) un TOKEN de los que
 * se encuentren registrdos en el mismo. La idea es que funcione como un
 * MAPA (Map) de elementos (pares clave-valor).
 *
 * @param <K> la clase de un identificador posible para el tipo de ITOKEN
 * @param <U> la clase del objeto (payload) a ser mantenido por el ITOKEN
 * @param <T> la clase que implementa los ITOKEN gestionados en el repositorio
 */
public interface ITokenRepo<K, U extends ITokenPayload, T extends IToken<K, U>> {
    /**
     * Permite agregar un TOKEN al repositorio.
     * 
     * @param clave la clave con la que se agrega el TOKEN
     * @param token el TOKEN a agregarse
     */
    void addToken(K clave, T token);

    /**
     * Permite obtener la cantidad de elementos en el repositorio.
     * 
     * @return la cantidad de elemntos en el repositorio
     */
    int size();

    /**
     * Permite consultar si existe un TOKEN en el repositorio
     * con la clave pasada como argumento.
     *
     * @param clave la clave con la que se agrega el TOKEN
     * @return retorna true si existe un TOKEN para esa clave, de lo contrario retorna false
     */
    boolean tokenExist(K clave);

    /**
     * Permite obtener un TOKEN del repositorio.
     *
     * @param clave la clave del TOKEN que quiere obtnerse
     * @return el TOKEN obtenido del repositorio con esa clave
     */
    T getToken(K clave);

    /**
     * Permite quitar un TOKEN del repositorio.
     *
     * @param clave la clave del TOKEN que se quiere quitar
     */
    void removeToken(K clave);

    /**
     * Permite obtener la lista completa de TOKENs gestionados.
     *
     * @return la lista completa de TOKENs gestionados
     */
    List<T> getList();
}
