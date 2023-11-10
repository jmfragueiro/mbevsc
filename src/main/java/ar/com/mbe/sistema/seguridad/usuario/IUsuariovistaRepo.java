package ar.com.mbe.sistema.seguridad.usuario;

import ar.com.mbe.base.repos.IRepositorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuariovistaRepo extends IRepositorio<Usuariovista, Long> {
    @Query("select uv" +
            " FROM Usuariovista uv " +
            " WHERE LOWER(uv.username) like %?1%" +
            " OR LOWER(uv.razonsocial) like %?1%" +
            " ORDER BY uv.razonsocial")
    Page<Usuariovista> findUsuariosByUsername(String username, Pageable pageable);

    @Query("select uv" +
            " FROM Usuariovista uv " +
            " WHERE uv.documento = ?1" +
            " ORDER BY uv.razonsocial")
    Page<Usuariovista> findUsuariosByDocumento(Integer username, Pageable pageable);

    Usuariovista findByUsername(String username);

    @Query("SELECT uv.username FROM Usuariovista uv WHERE uv.id = ?1 ")
    Optional<String> getUsernameByUsuarioId(Long id);
}
