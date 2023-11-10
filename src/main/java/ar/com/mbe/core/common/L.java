package ar.com.mbe.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase debe ser utilizada como un wrapper para las operaciones de logging del sistema.
 *
 * @author jmfragueiro
 * @version 20230601
 */
public class L {
    static final Logger logger = LoggerFactory.getLogger(L.class);

    public static String reduce(String mensaje, Object extra) {
        return mensaje.concat(C.SYS_CAD_SPACE)
                      .concat(C.SYS_CAD_OPENTYPE)
                      .concat(extra.toString())
                      .concat(C.SYS_CAD_CLOSETPE);
    }

    public static void info(String mensaje) {
        logger.info(mensaje);
    }

    public static void error(String mensaje) {
        logger.error(mensaje);
    }

    public static void warning(String mensaje) {
        logger.warn(mensaje);
    }

    public static void debug(String mensaje) {
        logger.debug(mensaje);
    }

    public static void info(String mensaje, Object extra) {
        logger.info(reduce(mensaje, extra));
    }

    public static void error(String mensaje, Object extra) {
        logger.error(reduce(mensaje, extra));
    }

    public static void warning(String mensaje, Object extra) {
        logger.warn(reduce(mensaje, extra));
    }

    public static void debug(String mensaje, Object extra) {
        logger.debug(reduce(mensaje, extra));
    }
}
