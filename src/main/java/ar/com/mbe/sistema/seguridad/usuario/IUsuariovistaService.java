package ar.com.mbe.sistema.seguridad.usuario;

import ar.com.mbe.base.service.IServicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUsuariovistaService extends IServicio<Usuariovista, Long> {
    Page<Usuariovista> findUsuarios(String filtro, Pageable pageable);

    Usuariovista findByUsername(String username);

    Optional<String> getUsernameByUsuarioId(Long id);
}
