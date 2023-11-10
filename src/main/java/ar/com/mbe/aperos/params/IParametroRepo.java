package ar.com.mbe.aperos.params;

import ar.com.mbe.base.repos.IRepositorio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IParametroRepo extends IRepositorio<Parametro, Long> {
    List<Parametro> findByTipoAndFechabajaIsNullOrderByOrden(Integer tipo);

    Optional<Parametro> findByTipoAndOrdenAndFechabajaIsNull(Integer tipo, Integer orden);

    List<Parametro> findByTipoAndBaseAndFechabajaIsNullOrderByOrden(Integer tipo, Long base);
}
