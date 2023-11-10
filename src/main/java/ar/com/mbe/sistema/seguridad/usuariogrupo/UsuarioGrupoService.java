package ar.com.mbe.sistema.seguridad.usuariogrupo;

import ar.com.mbe.base.service.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioGrupoService extends Servicio<UsuarioGrupo, Long> implements IUsuarioGrupoService {
    @Autowired
    public UsuarioGrupoService(IUsuarioGrupoRepo repo) { super(repo); }

    @Override
    public Class<UsuarioGrupo> getEntityClass() {
        return UsuarioGrupo.class;
    }
}
