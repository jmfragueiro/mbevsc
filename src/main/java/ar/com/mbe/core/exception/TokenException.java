package ar.com.mbe.core.exception;

/**
 * Esta clase de excepcion deberia utilizarse para representar todos los errores asociados a un problema
 * con un token o una parte del esquema de tokens del sistema, sea de acceso, de validación o un problema
 * lanzado por el sistema implementado.
 *
 * @author jmfragueiro
 * @version 20200201
 */
public class TokenException extends MessageException {
    public TokenException(String mensaje) {
        super(mensaje);
    }

    public TokenException(String mensaje, String extra) {
        super(mensaje, extra);
    }
}