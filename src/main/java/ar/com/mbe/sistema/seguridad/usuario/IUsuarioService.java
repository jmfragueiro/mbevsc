package ar.com.mbe.sistema.seguridad.usuario;

import ar.com.mbe.base.service.IServicio;
import ar.com.mbe.core.common.R;
import ar.com.mbe.core.token.ITokenPayloadService;
import ar.com.mbe.sistema.seguridad.permiso.Permiso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService extends IServicio<Usuario, Long>, ITokenPayloadService<Usuario> {
    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByName(String name);

    Optional<Usuario> findByTokenResetPassword(String token);

    Optional<Usuario> findByTokenCheckEmail(String token);

    Optional<Usuario> findByUsernameOrEmail(String parametro);

    Page<Usuario> findByUsuarioOrEmail(String filtro, Pageable pager);

    Optional<String> getUsernameByUsuarioId(Long id);

    List<String> getPermisosAsociados(Usuario usuario);

    List<Permiso> findPermisosPorUsuario(Usuario usuario);

    R registrarSolicitudRecuperarPassword(String username);

    R procesarRecuperarPassword(String token);

    R registrarSolicitudCheckEmail(String username, String email);

    R procesarCheckEmail(String token);

    String generarClaveTemporal(Usuario usuario);

    void registrarLogin(String username);

    void registrarLogout(String username);
}
