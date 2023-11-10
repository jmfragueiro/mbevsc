package ar.com.mbe.core.token;

import ar.com.mbe.core.jws.IJwsDataHelper;

import java.time.LocalDateTime;

/**
 * Un TOKEN es un elemento que permite mantener "registrado" otro objeto cualquiera denominado
 * PAYLOAD o carga útil (el cual deberia ser "conectado" al crearse el TOKEN -por ejem. en su
 * constructor- y que debe cumplir ciertas características de identificación y habilitación),
 * asociandole una clave única y una "validez" o vigencia temporal (presumiblemente, pero no
 * obligatoriamente). Permite verificar su sincronizacion con un momento determinado del tiempo
 * (sea lo que fuere que signifique) y extender el plazo de validez del mismo.
 *
 * @author jmfragueiro
 * @version 20230601
 * @param <K> la clase de un identificador posible para el tipo de ITOKEN
 * @param <U> la clase del objeto (payload) a ser mantenido por el ITOKEN
 */
public interface IToken<K, U extends ITokenPayload> extends IJwsDataHelper {
    /**
     * Permite obtener un identificador posible para el tipo de TOKEN, 
     * de manera que un tipo determinado pueda establecer un mecanismo 
     * consistente de identificación.
     * 
     * @return el identificador para el TOKEN
     */
    K getId();
    
    /**
     * Permite obtener el Objeto al cual se encuentra asociado el TOKEN,
     * es decir para el cual fue creado el TOKEN.
     *
     * @return el Objeto al cual se encuetra asociado el TOKEN
     */
    U getPayload();

    /**
     * Permite determinar si un TOKEN se encuentra "vencido", lo que significa
     * que terminó su plazo de vigencia y ya no puede tomarse como válido (por
     * ejemplo: porque se ha pasado su tiempo de expiración -ojo: del token-).
     *
     * @return retorna true si el TOKEN está vencido, de lo contrario retorna false
     */
    boolean isExpired();

    /**
     * Este método permite obtener el IAT (Issued At Time) el cual es una fecha que
     * nos dice en qué momento exacto se creó un TOKEN determinado. Esto sirve como
     * un punto mas de control para asegurar que un token determinado no fue modificado.
     *
     * @return el IAT del token como una fecha
     */
    LocalDateTime getIssuedAt();

    /**
     * Permite reiniciar el plazo de vencimiento de un TOKEN por una determinada
     * cantidad (lo que sea que esto signifique -aunque se espera que hablemos de
     * tiempo-).
     *
     * @param plazo un plazo de extension, para extender el vencimiento del TOKEN
     * @return retorna el token con el vencimiento reiniciado
     */
    IToken<K, U> reinitTerm(Long plazo);

    /**
     * Este método debe obtener el token apuntado si su "carga" (es decir el objeto
     * mantenido) es útil, es decir que puede utilizarse para le propósito deseado,
     * o de lo conteario debe lanzar una excepción explicando por qué no lo es.
     *
     * @return retorna el token en caso de ser mantener un objeto útil
     */
    IToken<K, U> getUseful();
}
