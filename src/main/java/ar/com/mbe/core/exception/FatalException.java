package ar.com.mbe.core.exception;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.L;
import ar.com.mbe.core.common.T;

/**
 * Esta clase de excepcion es la base de la jerarquia de excepciones del framework, para las
 * excepciones que DEBEN SER CAPTURADAS SI O SI dentro del sistema (son 'marcadas') y se usa
 * para encapsular el comportamiento general que debe tener una excepcion dentro del mismo.
 * <p>
 * La clase maneja da la posibilidad de crear una excepcion con un mensaje generico, con uno
 * especifico, o bien con uno especifico mas un texto extra aclaratorio. Ademas, esta clase
 * permite logear una excepcion, como un mensaje de nivel ERROR para el caso de que el nivel
 * de la aplicacion lo permita, utilizando para ello metodos estaticos de la clase LogSistema.
 *
 * @author jmfragueiro
 * @version 20200201
 */
public class FatalException extends Exception {
    /**
     * Esta version del contructor permite crear una excepcion sin mensaje ni datos extras
     * (utiliza en este caso un mensaje generico tomado de la constante MSJ_ERR_EXCEPCION).
     *
     * @see C
     */
    public FatalException() {
        this(C.MSJ_ERR_EXCEPCION, null);
    }

    /**
     * Esta version del contructor permite crear una excepcion con mensaje y sin datos extras.
     *
     * @param mensaje El mensaje que describe la excepcion.
     */
    public FatalException(String mensaje) {
        this(mensaje, null);
    }

    /**
     * Esta version del contructor permite crear una excepcion con mensaje y con datos extras.
     *
     * @param mensaje El mensaje que describe la excepcion.
     * @param extra   La cadena con datos extras para mostrarInnerLayout en la exepcion
     */
    public FatalException(String mensaje, String extra) {
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
                (extra == null ? C.SYS_CAD_NULL : extra));
    }
}