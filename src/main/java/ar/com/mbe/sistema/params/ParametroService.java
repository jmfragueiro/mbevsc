package ar.com.mbe.sistema.params;

import ar.com.mbe.base.service.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParametroService extends Servicio<Parametro, Long> implements IParametroService {
    @Autowired
    public ParametroService(IParametroRepo repo) {
        super(repo);
    }

    @Override
    public Class<Parametro> getEntityClass() {
        return Parametro.class;
    }

    @Override
    public List<Parametro> findByTipoOrderByOrden(Integer tipo) {
        return ((IParametroRepo) getRepo()).findByTipoAndFechabajaIsNullOrderByOrden(tipo);
    }

    @Override
    public Optional<Parametro> findByTipoAndOrden(Integer tipo, Integer orden) {
        return ((IParametroRepo) getRepo()).findByTipoAndOrdenAndFechabajaIsNull(tipo, orden);
    }

    @Override
    public List<Parametro> findByTipoAndBaseOrderByOrden(Integer tipo, Long base) {
        return ((IParametroRepo) getRepo()).findByTipoAndBaseAndFechabajaIsNullOrderByOrden(tipo, base);
    }
}
