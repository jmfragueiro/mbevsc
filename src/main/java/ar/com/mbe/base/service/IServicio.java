package ar.com.mbe.base.service;

import ar.com.mbe.base.entity.EntityException;
import ar.com.mbe.base.entity.IEntidad;
import ar.com.mbe.base.filter.RPredicadoConfig;
import ar.com.mbe.base.repos.IRepositorio;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Esta interfase representa el comprotamiento deseable de un servicio de persistencia para el modelo
 * de datos del sistema. Hereda aqui de IRepositorioAuditable porque se trabaja con entidades auditadas.
 * Las imlementaciones se espera que brinden los servs necesarios para persistir cambios al motor
 * de persistencia, eliminar instancias de entidades del motor y alplicar los mecanismos de auditoria,
 * siempre abtrayendo de las cuestiones 'fisicas' (de la implementacion concreta) al sistema en si.
 *
 * @param <T> El tipo de la entidad servida
 * @param <K> El tipo de la clave de identificacion para la entidad
 * @author jmfragueiro
 * @version 20200201
 */
public interface IServicio<T extends IEntidad<K>, K> extends Serializable, Cloneable {
    /**
     * Toda instancia de un IServicio debe poder "declarar" la clase de entidad bajo servicio,
     * la cual puede ser utilizado por otras partes del sistema.
     *
     * @return La clase de la entidad servida.
     */
     Class<T> getEntityClass();

    /**
     * Todas las implementaciones de Servicio de Repositorio deben tener un repositorio
     * por detrás que es el que efectivamente se comunica con la base de datos. Este es
     * el metodo que debe utilizarse para obtener una referencia a ese repositorio.
     *
     * @return una referencia al repositorio subyacente al servicio de repositorio.
     */
    IRepositorio<T, K> getRepo();

    /**
     * Este metodo debe retornar el Entity Manager actualmente utilizado por el servicio.
     *
     * @return el Entity Manager actualmente utilizado por el servicio.
     */
    EntityManager getEM();

    /**
     * Este metodo debe registrar al repositorio el estado actual de la instancia pasada como argumento,
     * realizando los cambios necesarios en dicho repositorio. Si el argumento flush es true, entonces
     * los cambios se confirman inmediatamente al motor de persistencia, de lo contrario se acumularan
     * hasta que el motor lo determine y se bajan todos juntos. Si existe algun problema con la accion,
     * deberia lanzarse una excepcion de persistencia.
     *
     * @param instancia la instancia a ser agregada al repositorio.
     * @param flush un boleano que indica si la persistencia debe ser confirmada inmediatamente
     * @return Retorna la propia instancia persistida (o marcada como tal).
     */
    T persist(T instancia, boolean flush) throws ServiceException;

    /**
     * Este metodo deberia 'eliminar' una instancia de una entidad persistente del repositorio de entidades
     * persistentes. Dentro del framework, lo que se entienda por eliminar deberá estar dado por, AL MENOS,
     * una combinación de dos acciones:
     * A) 'desactivar la instancia': vía su metodo kill
     * B) 'comunicar al motor de persistencia': haciendo persistente el cambio de estado de dicha instancia.
     * Si el argumento flush es true, entonces los cambios se confirman inmediatamente al motor de persistencia,
     * de lo contrario se acumularan hasta que el motor lo determine y se bajan todos juntos. Si existe algun
     * problema con la accion, deberia lanzarse una excepcion de persistencia.
     *
     * @param instancia la instancia a ser eliminada.
     * @param flush un boleano que indica si la persistencia debe ser confirmada inmediatamente
     */
    default void delete(T instancia, boolean flush) throws ServiceException { instancia.kill(); persist(instancia, flush); }

    /**
     * Este metodo devuelve un objeto identificable del tipo U a partir de su ID interno. Si existe
     * algun problema deberia lanzar una excepcion de entidad.
     *
     * @param id El ID interno del objeto (registro) de tipo T deseado.
     * @return El objeto de tipo T que cuyo ID coincide con el pasado.
     */
    Optional<T> findById(K id) throws EntityException;

    /**
     * Este metodo devuelve todos los objetos identificables del tipo U que se encuentren 'vivos'.
     * El significado de 'vivo' debería tener sentido para la aplicacion y deberia aplicarse aquí
     * en funcion de lo que dictamine el metodo isAlive() de la entidad. Si existe algun problema
     * deberia lanzar una excepcion de entidad.
     *
     * @return La colección de objetos vivos de tipo T obtenidos (sin filtros).
     */
    List<T> findAlive() throws EntityException;

    /**
     * Este metodo devuelve todos los objetos identificables del tipo U que pasen el filtro pasado
     * como argumento. El parametro filtro debe tener una codificación especifica del tipo:
     * "campo,valor;campo,valor;campo,valor;...".
     *
     * @param filtro la cadena con el filtro a aplicarse
     * @param pageable un objeto pageable para permitir la paginación de resultados
     * @return La colección de objetos de tipo U obtenidos segun el filtro.
     */
    Page<T> filter(String filtro, Pageable pageable) throws ClassNotFoundException;

    /**
     * Este metodo deber permitir obtener un registro de configuración de predicado de filtro a
     * partir del nombre de un atributo de la entidad bajo servicio. De esa forma, el metodo de
     * filtro (filter) puede "acomodarse" a cada Entidad.
     *
     * @param atributo el atributo para el cual se quiere la configuración de predicado
     * @return la configuración de predicado predefinida para generar el filtro deseado
     */
    RPredicadoConfig getPredicadoConfig(String atributo);

    /**
     * Este metodo permite realizar una normalizacion de los datos de una instancia antes de ser persistidos.
     * Este metodo sera llamado siempre antes de la persistencia final de una instancia y se espera tambien
     * aqui se realicen todos los controles previos necesarios (que no hayan podido realizarse antes por la
     * razón que sea).
     *
     * @param instancia la instancia con los datos a ser persistidos
     * @return la instancia con los datos normalizados (lo que sea que signifique en cada caso)
     */
    default T normalizarDatos(T instancia) {
        return instancia;
    }
}
