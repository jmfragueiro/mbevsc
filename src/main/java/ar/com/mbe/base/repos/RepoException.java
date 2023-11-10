package ar.com.mbe.base.repos;


import ar.com.mbe.core.exception.MessageException;

/**
 * Esta clase de excepcion deberia utilizarse para representar todos los errores asociados a un problema
 * con un repositorio del sistema, sea de conectividad, acceso, del JPA subyacente o un problema lanzado
 * por el propio driver de dicho motor.
 *
 * @author jmfragueiro
 * @version 20230601
 */
public class RepoException extends MessageException {
    public RepoException(String mensaje) {
        super(mensaje);
    }

    public RepoException(String mensaje, String extra) {
        super(mensaje, extra);
    }
}