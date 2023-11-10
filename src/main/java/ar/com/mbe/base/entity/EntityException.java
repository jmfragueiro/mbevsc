package ar.com.mbe.base.entity;

import ar.com.mbe.core.exception.MessageException;

/**
 * Esta clase de excepcion deberia utilizarse para representar todos los errores asociados a un problema
 * con alguna de las propiedades basicas de una entidad dentro del framework, los que puede asociarse a
 * operaciones propias de la entidad y mas alla de cualquier otro contexto interviniente.
 * NOTA: por ser una MessageException (y heredar de RuntimeException -unchecked-) el lanzamiento de esta
 * genera un rollback automatico de la transacción JPA en curso (funcionamiento por defecto de Spring).
 *
 * @author jmfragueiro
 * @version 20230601
 */
public class EntityException extends MessageException {
    public EntityException(String mensaje) {
        super(mensaje);
    }

    public EntityException(String mensaje, String extra) {
        super(mensaje, extra);
    }
}
