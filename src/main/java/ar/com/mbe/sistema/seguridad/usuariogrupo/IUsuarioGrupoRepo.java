package ar.com.mbe.sistema.seguridad.usuariogrupo;

import ar.com.mbe.base.repos.IRepositorio;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioGrupoRepo extends IRepositorio<UsuarioGrupo, Long> {
    Optional<UsuarioGrupo> findById(Long id);
}
