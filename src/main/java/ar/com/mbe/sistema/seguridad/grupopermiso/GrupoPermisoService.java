package ar.com.mbe.sistema.seguridad.grupopermiso;

import ar.com.mbe.base.service.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrupoPermisoService extends Servicio<GrupoPermiso, Long> implements IGrupoPermisoService {
    @Autowired
    public GrupoPermisoService(IGrupoPermisoRepo repo) { super(repo); }

    @Override
    public Class<GrupoPermiso> getEntityClass() {
        return GrupoPermiso.class;
    }
}
