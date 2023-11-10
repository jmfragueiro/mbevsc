package ar.com.mbe.core.exception;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.L;
import ar.com.mbe.core.common.T;
import org.springframework.security.core.AuthenticationException;

/**
 * Esta clase de excepcion deberia utilizarse para representar todos los errores asociados a un problema
 * con algú componente de seguridad del sistema, sea de acceso, validación o un problema lanzado por el
 * sistema implementado.
 *
 * @author jmfragueiro
 * @version 20200201
 */
public class AuthException extends AuthenticationException {
    /**
     * Esta version del contructor permite crear una excepcion sin mensaje ni datos extras
     * (utiliza en este caso un mensaje generico tomado de la constante MSJ_ERR_EXCEPCION).
     *
     * @see C
     */
    public AuthException() {
        this(C.MSJ_SES_ERR_NOAUTH);
    }

    /**
     * Esta version del contructor permite crear una excepcion con mensaje y sin datos extras.
     *
     * @param mensaje El mensaje que describe la excepcion.
     */
    public AuthException(String mensaje) {
        this(mensaje, null);
    }

    /**
     * Esta version del contructor permite crear una excepcion con mensaje y con datos extras.
     *
     * @param mensaje El mensaje que describe la excepcion.
     * @param extra   La cadena con datos extras para mostrarInnerLayout en la exepcion
     */
    public AuthException(String mensaje, String extra) {
        super(mensaje);
        registrarMensaje(mensaje, extra);
    }

    /**
     * Este metodo registra al sistema de Logging la excepcion lanzada
     */
    private void registrarMensaje(String mensaje, String extra) {
        L.error(C.SYS_CAD_OPENTYPE
                        .concat(T.getNombreMetodoLlamante(4))
                        .concat(C.SYS_CAD_CLOSETPE)
                        .concat(mensaje),
                T.getEmptyStringOnNull(extra));
    }
}