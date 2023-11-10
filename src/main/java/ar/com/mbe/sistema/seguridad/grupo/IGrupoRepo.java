package ar.com.mbe.sistema.seguridad.grupo;

import ar.com.mbe.base.repos.IRepositorio;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IGrupoRepo extends IRepositorio<Grupo, Long> {
    Optional<Grupo> findById(Long id);

    Optional<Grupo> findByName(String name);
}
