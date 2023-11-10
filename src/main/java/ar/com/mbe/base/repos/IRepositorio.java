package ar.com.mbe.base.repos;

import ar.com.mbe.base.entity.IEntidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Esta interfase representa el comportamiento deseable de un repositorio de persistencia para el modelo
 * de datos del sistema. Hereda aqui de JpaRepository porque se trabaja con una base JPA subyacente, pero
 * justamente permite abstraer hacia arriba de la capa que finalmente implemente en la realidad las opers
 * que se necesiten. Las imlementaciones se espera que brinden los servs necesarios para persistir cambios
 * al motor de persistencia, eliminar instancias de entidades del motor y aplicar mecanismos de auditoria,
 * siempre abtrayendo de las cuestiones 'fisicas' (de la implementacion concreta) al sistema en si.
 *
 * @param <T> El tipo de la entidad servida
 * @param <K> El tipo de la clave de identificacion para la entidad
 * @author jmfragueiro
 * @version 2020301
 */
@NoRepositoryBean
public interface IRepositorio<T extends IEntidad<K>, K> extends JpaRepository<T, K> {
    List<T> findByFechabajaIsNull();

    Page<T> findByFechabajaIsNull(Pageable pageable);

    default List<T> findAlive() {
        return findByFechabajaIsNull();
    }

    default Page<T> pageAlive(Pageable page) {
        return findByFechabajaIsNull(page);
    }
}
