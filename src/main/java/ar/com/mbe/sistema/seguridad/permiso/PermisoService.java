package ar.com.mbe.sistema.seguridad.permiso;

import ar.com.mbe.base.service.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermisoService extends Servicio<Permiso, Long> implements IPermisoService {
    @Autowired
    public PermisoService(IPermisoRepo repo) { super(repo); }

    @Override
    public Class<Permiso> getEntityClass() {
        return Permiso.class;
    }
}
