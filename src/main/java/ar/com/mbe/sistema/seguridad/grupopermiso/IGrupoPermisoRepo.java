package ar.com.mbe.sistema.seguridad.grupopermiso;

import ar.com.mbe.base.repos.IRepositorio;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IGrupoPermisoRepo extends IRepositorio<GrupoPermiso, Long> {
    Optional<GrupoPermiso> findById(Long id);
}
