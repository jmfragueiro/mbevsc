package ar.com.mbe.sistema.seguridad.usuario;

import ar.com.mbe.base.repos.IRepositorio;
import ar.com.mbe.sistema.seguridad.permiso.Permiso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepo extends IRepositorio<Usuario, Long> {
    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByUsername(String username);

    Usuario findByUsernameIgnoreCase(String username);

    @Query("SELECT u FROM Usuario u WHERE u.tokenresetpassword = ?1 ")
    Optional<Usuario> findByTokenResetPassword(String token);

    @Query("SELECT u FROM Usuario u WHERE u.emailchecktoken = ?1 ")
    Optional<Usuario> findByTokenCheckEmail(String token);

    @Query("SELECT u.username FROM Usuario u WHERE u.id = ?1 ")
    Optional<String> getUsernameByUsuarioId(Long id);

    @Query("select u from Usuario u where lower(u.username) = ?1 or lower(u.email) = ?1 ")
    Optional<Usuario> findByUsernameOrEmail(String parametro);

    @Query("select p " +
            " from ar.com.mbe.sistema.seguridad.permiso.Permiso p " +
            "where p.fechabaja is null " +
            "  and p in (select g.permiso " +
            "              from GrupoPermiso g" +
            "             where g.grupo in (select u.grupo" +
            "                                 from UsuarioGrupo u" +
            "                                where u.usuario = ?1 " +
            "                                  and u.fechabaja is null))")
    List<Permiso> findPermisosPorUsuario(Usuario u);

    @Query("select u from Usuario u where u.username like %?1% or u.email like %?1% ")
    Page<Usuario> findByUsuarioOrEmail(String filtro, Pageable pager);
}
