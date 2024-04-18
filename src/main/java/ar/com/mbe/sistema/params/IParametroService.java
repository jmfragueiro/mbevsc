package ar.com.mbe.sistema.params;

import ar.com.mbe.base.service.IServicio;

import java.util.List;
import java.util.Optional;

public interface IParametroService extends IServicio<Parametro, Long> {
    List<Parametro> findByTipoOrderByOrden(Integer tipo);

    Optional<Parametro> findByTipoAndOrden(Integer tipo, Integer orden);

    List<Parametro> findByTipoAndBaseOrderByOrden(Integer tipo, Long base);
}
