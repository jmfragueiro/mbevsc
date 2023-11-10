package ar.com.mbe.aperos.codes;

import java.util.Optional;

public interface ICodeClass {
    /**
     * Este metodo debe retornar la prioridad de la clase, por la que se ordenará
     * la lista de opciones de manera de que si dos clases que admiten codigos son
     * aptas para una funcion, entonces se tomara la de menor prioridad. NOTA: se
     * debría asiganar, a las clases, prioridades al estilo de la progamación BASIC
     * (dejando huecos para las futuras clases -ej: 10,20,30,40 y así...-).
     *
     * @return la prioridad de la clase (menor es primero)
     */
    int getPrioridad();

    /**
     * Este metodo debería retornar verdadero si la clase puede manejar la informacion
     * del token enviado como argumento (ya sea para generar un QR, obtener información
     * asociada, etc.). Para ello la clase sabrá como decodificar el token y buscar si
     * lo entiende y lo puede manejar.
     *
     * @param token el token que deberá manejarse (con la información que permite la identificación)
     * @return true si la clase puede manejar ese token, de lo contrario false
     */
    boolean isThatCodeClass(String token);

    /**
     * Una calse capaz de manejar un token tiene que poder obtener información relevante
     * asociada a los datos contenidos en el token. Este es el metodo que permite obtener
     * esa información.
     *
     * @param token el token con los datos origen
     * @return una instancia de alguna clase con información asociada al token (segun esta clase)
     */
    Optional<Object> findByToken(String token);
}
