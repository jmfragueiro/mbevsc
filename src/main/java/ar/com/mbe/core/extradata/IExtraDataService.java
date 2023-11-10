package ar.com.mbe.core.extradata;

/**
 * Esta interfase representa el comportamiento deseado de un servicio capaz de
 * generar y, posteriormente, extraer un dato extra para que, por ejemplo, sea
 * incorporado al un JWS en un item llamado 'ted', y de extraer, luego, ese dato
 * extra a partir de un Object en cada requerimiento HTTP.
 */
public interface IExtraDataService {
    /**
     * Este metodo debe ser el encargado de generar el dato extra a partir
     * de la informacion contenida en el token para el cual se genera dicho
     * dato extra.
     *
     * @param origen el objeto para el cual se genera el dato extra
     * @return el dato extra generado
     */
    Object create(Object origen);

    /**
     * Este metodo debe ser el encargado de extraer el dato extra a partir
     * de la cadena obtenida desde el item ted de un JWS.
     *
     * @param ted el string del item 'ted' del jws origen de los datos extras
     * @return el dato extra extraido
     */
    Object extract(Object ted);
}
