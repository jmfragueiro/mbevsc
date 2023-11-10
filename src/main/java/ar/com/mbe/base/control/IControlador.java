package ar.com.mbe.base.control;

import ar.com.mbe.base.entity.IEntidad;
import ar.com.mbe.base.service.IServicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * Esta interfase representa el comprotamiento deseable de un controlador de servicios REST
 * que permita asociar una vista con un servicio de repositorio de persistencia dentro del
 * framework ad-hoc.
 *
 * @param <T>   El tipo de la entidad servida por el servicio
 * @param <K> El tipo de la clave de identificacion para la entidad
 * @author jmfragueiro
 * @version 20230601
 */
public interface IControlador<T extends IEntidad<K>, K> {
    /**
     * Todas las implementaciones de Controlador de Servicio deben tener un servicio
     * por detrás que es el que efectivamente se comunica con los repositorios. Este
     * es el metodo que debe utilizarse para obtener una referencia a ese servicio.
     *
     * @return una referencia al servicio subyacente al controlador.
     */
    IServicio<T, K> getServicio();

    /**
     * Toda instancia de un controlador debe poder responder a un pedido REST GET "puro"
     * retornando el listado total de entidades 'vivas' (depende del método isAlive()),
     * y teniendo en cuenta que la respuesta deve volver en formato JSON.
     *
     * @return La colección JSON de todos los objetos persistidos de la clase U 'vivos'
     */
    ResponseEntity<List<T>> list();

    /**
     * Toda instancia de un controlador debe responder a un pedido REST GET '.../fltpg?...',
     * con condiciones de filtrado y ordenado. En la soliciud, la definición del flt y de
     * las condiciones de paginación se logran con, por ejemplo:
     *   GET https://localhost/ciudad/fltpg?flt=provincia,Jujuy&page=1&size=3&sort=nombre,desc
     * La respuesta devuelve tambien los datos de un objeto Pageable que representa a la
     * página de instancias generada.
     *
     * @param flt una cadena de condición que será interpretada como filtro de la consulta.
     * @param pageable un objeto para definir tanto el orden como la pagina a devolverse.
     * @return Una página de la colección de los objetos U persistidos 'vivos' que responden
     * a las condiciones pasadas y en función de la página y el orden solicitado.
     */
    ResponseEntity<Page<T>> filter(String flt, Pageable pageable) throws ClassNotFoundException;

    /**
     * Toda instancia de un ayudante de controlador debe poder responder a un pedido REST
     * GET /KEY con un valor, y uno solo, del de la clave de búsqueda de la clase U. Aqui
     * 'KEY' representa una clave única por la cual obtener un objeto de la clase U y donde
     * la definición sobre el estado de vida del mismo aceptable se deja a la implementación
     * concreta de la clase.
     *
     * @param key un valor de clave por la cual encontrar al objeto a devolver.
     * @return El objeto de la clase U requerido (en HATEOAS)
     */
    ResponseEntity<T> get(K key);

    /**
     * Toda instancia de un ayudante de controlador debe poder responder a un pedido REST
     * POST acompañado de un objeto, y uno solo, de la clase U que será entonces persistido
     * al mecanismo de persistencia subyacente. La serialización del objeto U se hace según
     * los datos, en formato JSON, que vengan en el cuerpo del requerimiento. De existir un
     * error de serialización, validación, etc., SPRING genera un objeto BindingResult que
     * debe ser verificado antes de continuar con el requerimiento y, en caso de no continuar,
     * la respuesta, además de devolver código HTTP de error, debe generarse a partir del los
     * datos contenidos en dicho objeto.
     *
     * @param objecto un objeto U que se intenta persistir dentro del framework.
     * @param result  un BindingResult con el resultado de la serialización de los datos.
     * @return El objeto de la clase U persistido.
     */
    ResponseEntity<Object> add(T objecto, BindingResult result);

    /**
     * Toda instancia de un ayudante de controlador debe poder responder a un pedido REST
     * PUT acompañado de un objeto, y uno solo, de la clase U, el cual se espera que ya
     * exista en el mecanismo de persistencia subyacente y cuyos datos serán actualizados
     * con los valores contenidos en el objeto argumento. La serialización del objeto U se
     * hace según los datos, en formato JSON, que vengan en el cuerpo del requerimiento.
     * De existir un error de serialización, validación, etc., SPRING genera un objeto
     * BindingResult que debe ser verificado antes de continuar con el requerimiento y, en
     * caso de no continuar, la respuesta, además de devolver código HTTP de error, debe
     * generarse a partir del los datos contenidos en dicho objeto.
     *
     * @param objecto un objeto U que se intenta persistir dentro del framework.
     * @param result  un BindingResult con el resultado de la serialización de los datos.
     * @return El JSON del objeto de la clase U actualizado.
     */
    ResponseEntity<Object> update(T objecto, BindingResult result);

    /**
     * Toda instancia de un ayudante de controlador debe poder responder a un pedido REST
     * DELETE /KEY con un valor, y uno solo, del de la clave de búsqueda de la clase U. Aqui
     * 'KEY' representa una clave única por la cual encontrar al objeto de la clase U el cual
     * deberá ser marcado como 'no vivo' (es decir que a partir de esta operación, el valor
     * del método isAlive() para ese objeto deberá ser 'falso'). Se espera que el objeto se
     * encuentre 'vivo' (isAlive()=true) o de lo contrario no debería ser encontrado, lo que
     * debería devoveler un error en la respuesta HTTP.
     *
     * @param key un valor de clave por la cual encontrar al objeto a 'matar'.
     * @return El objeto de la clase U requerido (en HATEOAS).
     */
    ResponseEntity<Object> delete(K key);
}