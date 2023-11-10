package ar.com.mbe.aperos.apirest.excepciones;

import ar.com.mbe.core.exception.MessageException;

/**
 * Esta clase de excepcion deberia utilizarse para representar todos los errores asociados a un problema
 * con una llamada API REST a algún servicio externo (externo a quien llama, puede ser un servicio propio).
 *
 * @author jmfragueiro
 * @version 20161011
 */
public class RestException extends MessageException {
    public RestException(String mensaje) {
        super(mensaje);
    }

    public RestException(String mensaje, String extra) {
        super(mensaje, extra);
    }
}
