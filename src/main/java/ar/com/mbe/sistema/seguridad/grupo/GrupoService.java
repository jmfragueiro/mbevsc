package ar.com.mbe.sistema.seguridad.grupo;

import ar.com.mbe.base.service.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrupoService extends Servicio<Grupo, Long> implements IGrupoService {
    @Autowired
    public GrupoService(IGrupoRepo repo) { super(repo); }

    @Override
    public Class<Grupo> getEntityClass() {
        return Grupo.class;
    }
}
