package ar.com.mbe.base.service;

import ar.com.mbe.base.entity.IEntidad;
import ar.com.mbe.base.filter.CustomFilter;
import ar.com.mbe.base.filter.EPredicadoOperador;
import ar.com.mbe.base.filter.EPredicadoTipo;
import ar.com.mbe.base.filter.RPredicadoConfig;
import ar.com.mbe.base.repos.IRepositorio;
import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.R;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de interfase IServicio para un sistema con JPA-Spring. Esta clase es ademas
 * la que se encuentra encargada de representar al EntityManager que se usa en la aplicacion
 * para el manejo de la persistencia contra una base de datos relacional.
 *
 * @param <T> El tipo de la entidad servida por el servicio
 * @param <K> El tipo de la clave de identificacion para la entidad
 * @author jmfragueiro
 * @version 20230601
 */
public abstract class Servicio<T extends IEntidad<K>, K extends Serializable> implements IServicio<T, K> {
    private final IRepositorio<T, K> repo;

    @PersistenceContext
    private EntityManager entityManager;

    protected Servicio(IRepositorio<T, K> repo) {
        this.repo = repo;
    }

    /**
     * Este metodo se ejecuta con anterioridad al persist de la Entidad y eventualmente es usado para validar
     * los datos a persistir. La operación de persistencia únicamente debería continuar si el resultado del
     * metodo retorna con éxito (R.isSuccess() == true).
     *
     * @param instancia         la instancia con los datos a ser persistidos
     * @param instanciaoriginal la instancia original con los datos antes de la modificación
     * @return una instancia de R indicando el resultado de la operación
     */
    protected R prePersist(T instancia, T instanciaoriginal) {
        return new R(true, C.SYS_CAD_OK);
    }

    /**
     * Este metodo se ejecuta con posterioridad al persist de la Entidad y eventualmente es usado para
     * ejecutar tareas finales que dependan de la finalización correcta del persist.
     *
     * @param instancia la instancia con los datos a ser persistidos
     * @return la instancia persistida
     */
    protected T postPersist(T instancia) {
        return instancia;
    }

    public EntityManager getEM() {
        return this.entityManager;
    }

    @Override
    public IRepositorio<T, K> getRepo() {
        return repo;
    }

    @Override
    public T persist(T instancia, boolean flush) throws ServiceException {
        instancia = normalizarDatos(instancia);

        T original = this.findById(instancia.getId()).orElse(null);
        if (original != null) {
            entityManager.detach(instancia);
        }

        var resultado = this.prePersist(instancia, original);
        if (!resultado.success()) {
            throw new ServiceException(C.MSJ_ERR_ATSAVEDATA, resultado.mensaje());
        }

        return this.postPersist(((flush) ? repo.saveAndFlush(instancia) : repo.save(instancia)));
    }

    @Override
    public Optional<T> findById(K id) {
        return repo.findById(id);
    }

    @Override
    public List<T> findAlive() {
        return repo.findAlive();
    }

    @Override
    public Page<T> filter(String filtro, Pageable pag) throws ClassNotFoundException {
        return filtro == null || filtro.isEmpty()
               ? this.getRepo().pageAlive(pag)
               : new CustomFilter().filter(filtro, pag, this);
    }

    @Override
    public RPredicadoConfig getPredicadoConfig(String atributo) {
        return switch (atributo) {
            // para los atributos fijos conocidos
            case "id" -> new RPredicadoConfig(EPredicadoOperador.EQUAL, EPredicadoTipo.LONG, false);
            case "fechaumod", "fechabaja" -> new RPredicadoConfig(EPredicadoOperador.EQUAL, EPredicadoTipo.DATE, false);
            case "userumod" -> new RPredicadoConfig(EPredicadoOperador.EQUAL, EPredicadoTipo.STRING, false);
            // para el caso general
            default -> new RPredicadoConfig(EPredicadoOperador.EQUAL, EPredicadoTipo.STRING, true);
        };
    }
}
