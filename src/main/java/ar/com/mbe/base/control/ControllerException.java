package ar.com.mbe.base.control;


import ar.com.mbe.core.exception.MessageException;

/**
 * Esta clase de excepcion deberia utilizarse para representar todos los errores asociados a un problema
 * con un controlador del sistema, sea de conectividad, acceso o de cualquier tipo de verificacione que
 * se asocie a las entidades manejadas.
 * NOTA: por ser una MessageException (y heredar de RuntimeException -unchecked-) el lanzamiento de esta
 * genera un rollback automatico de la transacción JPA en curso (funcionamiento por defecto de Spring).
 *
 * @author jmfragueiro
 * @version 20230601
 */
public class ControllerException extends MessageException {
    public ControllerException(String mensaje) {
        super(mensaje);
    }

    public ControllerException(String mensaje, String extra) {
        super(mensaje, extra);
    }
}