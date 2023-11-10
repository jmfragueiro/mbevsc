package ar.com.mbe.sistema.seguridad.permiso;

import ar.com.mbe.base.repos.IRepositorio;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPermisoRepo extends IRepositorio<Permiso, Long> {
    Optional<Permiso> findById(Long id);

    Optional<Permiso> findByPermiso(String role);
}
