package ar.com.mbe.core.common;

import ar.com.mbe.core.auth.EHttpReqAuthType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Esta clase debe ser utilizada como un punto focal para la generación de mensajes
 * de error para rellenar las respuestas a peticiones HTTP, para tener encapsuladas,
 * en una sola clase, todas las cuestiones asociadas a este tipo de acciones.
 *
 * @author jmfragueiro
 * @version 20230601
 */
public final class E {
    /**
     * Este metodo permite obtener un objeto H, con el cuerpo de respuesta HTTP
     * en formato JSON para un error o excepción del sistema, en forma unificada.
     *
     * @param status  el codigo de estado HTTP asociado al error
     * @param error   el texto principal del error
     * @param mensaje un mensaje para el error
     * @param path    el camino URL asociado al error
     * @return el Map con los valores de error formateados
     */
    public static H getJsonErrorHttpBody(Integer status, String error, String mensaje, String path) {
        var err = (error == null || error.isBlank()) ? C.MSJ_SES_ERR_GENERIC : error;

        return new H(LocalDateTime.now().toString(), status, mensaje, null, err, path, EHttpReqAuthType.BEARER);
    }

    /**
     * Este metodo permite generar una salida al objeto HttpServletResponse, que
     * representa la respuesta de una peticion HTTP, con un cuerpo de respuesta
     * en formato JSON para un error o excepción del sistema, en forma unificada.
     *
     * @param status  el codigo de estado HTTP asociado al error
     * @param error   el texto principal del error
     * @param mensaje un mensaje para el error
     * @param path    el camino URL asociado al error
     * @return el Map con los valores de error formateados
     */
    public static void respondGenericError(HttpServletResponse response, Integer status, String error, String mensaje,
                                           String path) throws IOException {
        response.setContentType(C.SYS_APP_MIMETYPE_JSON);
        response.setStatus(status);

        new ObjectMapper().writeValue(response.getOutputStream(), getJsonErrorHttpBody(status, mensaje, error, path));
    }

    /**
     * Este metodo permite obtener una cadena, con el cuerpo de respuesta HTTP
     * en formato JSON para un error o excepción del sistema, en forma unificada.
     *
     * @param status  el codigo de estado HTTP asociado al error
     * @param error   el texto principal del error
     * @param mensaje un mensaje para el error
     * @param path    el camino URL asociado al error
     * @return el Map con los valores de error formateados
     */
    public static String getJsonErrorString(Integer status, String error, String mensaje, String path) throws IOException {
        var err = (error == null || error.isBlank()) ? C.MSJ_SES_ERR_GENERIC : error;

        return new ObjectMapper().writeValueAsString(E.getJsonErrorHttpBody(status, mensaje, err, path));
    }
}

