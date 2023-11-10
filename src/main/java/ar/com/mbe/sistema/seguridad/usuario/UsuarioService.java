package ar.com.mbe.sistema.seguridad.usuario;

import ar.com.mbe.aperos.codes.ECodeFunction;
import ar.com.mbe.aperos.email.Email;
import ar.com.mbe.aperos.email.EmailService;
import ar.com.mbe.base.entity.EntityException;
import ar.com.mbe.base.repos.ItemNotFoundException;
import ar.com.mbe.base.service.Servicio;
import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.F;
import ar.com.mbe.core.common.R;
import ar.com.mbe.core.common.T;
import ar.com.mbe.core.security.SecurityService;
import ar.com.mbe.core.token.ITokenPayload;
import ar.com.mbe.core.token.Token;
import ar.com.mbe.sistema.seguridad.permiso.Permiso;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Qualifier("popo")
public class UsuarioService extends Servicio<Usuario, Long> implements IUsuarioService {
    @Value("${front.urlconsultaqr}")
    private String urlconsultaqr;

    @Autowired
    private EmailService emailService;

    @Autowired
    public UsuarioService(IUsuarioRepo repo) {
        super(repo);
    }

    @Override
    public Optional<Usuario> findByTokenResetPassword(String token) {
        return ((IUsuarioRepo) getRepo()).findByTokenResetPassword(token);
    }

    @Override
    public Optional<Usuario> findByTokenCheckEmail(String token) {
        return ((IUsuarioRepo) getRepo()).findByTokenCheckEmail(token);
    }

    @Override
    public List<String> getPermisosAsociados(Usuario usuario) {
        try {
            return findPermisosPorUsuario(usuario).stream()
                                                  .map(Permiso::getPermiso)
                                                  .map(p -> Arrays.asList(p.split(":")))
                                                  .flatMap(Collection::stream)
                                                  .collect(Collectors.toList());
        } catch (BeansException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Class<Usuario> getEntityClass() {
        return Usuario.class;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return getRepo().findById(id);
    }

    @Override
    public Optional<Usuario> findByName(String name) {
        return ((IUsuarioRepo) getRepo()).findByUsername(name);
    }

    @Override
    public Optional<Usuario> findByUsernameOrEmail(String parametro) {
        return ((IUsuarioRepo) getRepo()).findByUsernameOrEmail(parametro);
    }

    @Override
    public Page<Usuario> findByUsuarioOrEmail(String filtro, Pageable pageable) {
        return ((IUsuarioRepo) this.getRepo()).findByUsuarioOrEmail(filtro, pageable);
    }

    @Override
    public Optional<String> getUsernameByUsuarioId(Long id) {
        return ((IUsuarioRepo) getRepo()).getUsernameByUsuarioId(id);
    }

    @Override
    public Usuario normalizarDatos(Usuario usuario) {
        usuario.setUsername(T.getlimpiarString(usuario.getName()).toLowerCase().trim());
        usuario.setEmail(T.getlimpiarString(usuario.getEmail()).toLowerCase().trim());

        return usuario;
    }

    @Override
    public List<Permiso> findPermisosPorUsuario(Usuario usuario) {
        return ((IUsuarioRepo) getRepo()).findPermisosPorUsuario(usuario);
    }

    @Override
    public void registrarLogin(String username) {
        Usuario usuario = ((IUsuarioRepo) getRepo()).findByUsernameIgnoreCase(username);
        usuario.setLastLogin(LocalDateTime.now());
        usuario.setTokenresetpassword(null);
        this.persist(usuario, true);
        this.postPersist(usuario);//, null, LOGIN);
    }

    @Override
    public void registrarLogout(String username) {
        this.postPersist(((IUsuarioRepo) getRepo()).findByUsernameIgnoreCase(username));//, null, LOGOUT);
    }

    @Override
    public Usuario postPersist(Usuario usuario) {
        /**
         * Tarea 1 del Post Persist
         * Audito la Actualizacion de Usuarios
         */
        String observacion;
        String datofijo = C.SYS_CAD_SPACE.concat(C.SYS_CAD_NUMSEP)
                                         .concat(C.SYS_CAD_SPACE)
                                         .concat(C.SYS_CAD_SEC_USUARIO)
                                         .concat(C.SYS_CAD_LOGSEP)
                                         .concat(C.SYS_CAD_SPACE)
                                         .concat(usuario.getName());

        return usuario;
    }

    @Override
    public R registrarSolicitudRecuperarPassword(String username) {
        R resultado;

        Optional<Usuario> usuario = ((IUsuarioRepo) getRepo()).findByUsername(username);

        if (usuario.isEmpty() || !usuario.get().isAlive()) {
            resultado = new R(false, C.MSJ_SEC_ERR_NOUSER);
        } else if (!usuario.get().getEnabled()) {
            resultado = new R(false, C.MSJ_SEC_ERR_USERENABLED);
        } else if (usuario.get().getLocked()) {
            resultado = new R(false, C.MSJ_SEC_ERR_USERLOCKED);
        } else if (usuario.get().getEmailcheckAt() == null) {
            resultado = new R(false, C.MSJ_SEC_ERR_USEREMAILNOTCHECK.concat(C.SYS_CAD_PUNTO)
                                                                    .concat(C.SYS_CAD_SPACE)
                                                                    .concat(C.MSJ_SEC_ERR_CANTSENDRECEMAIL));
        } else {
            try {
                /* Registro el Token de Recuperación */
                String textoacodificar = usuario.get().getId().toString() + F.getNowDateAsString("");
                String mi_token = ECodeFunction.GEN_RESET_PASS.getPrefijo().concat(T.getMd5Token(textoacodificar));
                usuario.get().setTokenresetpassword(mi_token);

                //////////////////////////////////////////////////////////
                // Envio Correo de Aviso al Usuario                     //
                //////////////////////////////////////////////////////////
                /* Armo contenido del Correo */
                String contenido = "<div>"
                                   + "<h2><strong>Estimado/a "
                                   + usuario.get().getEmail()
                                   //.getPersona()
                                   //.getRazonsocial()
                                   + "</strong></h2>"
                                   + "</br>"
                                   + "<p>Usted ha Solicitado Iniciar el Proceso de Recuperación"
                                   + " de la Clave de Acceso</p>"
                                   + "</br>"
                                   + "<p>para el usuario: <strong>"
                                   + usuario.get().getName()
                                   + "</strong></p>"
                                   + "</br>"
                                   + "<p>Para Efectivizar la Recuperación de la Clave utilice "
                                   + "el siguiente Link "
                                   + this.urlconsultaqr
                                   + mi_token
                                   + "</p>"
                                   + "</br>"
                                   + "<p>No responda este correo electrónico, ya que fue "
                                   + "enviado automáticamente</p>"
                                   + "</br>";

                /* Preparo Datos para el Correo */
                Email email = new Email();
                email.setDestinatarios(new String[]{usuario.get().getEmail()});
                email.setConCopiaA(null);
                email.setResponderA(null);
                email.setAsunto("Solicitud de Recuperación del Password");
                email.setContenido(contenido);

                /* Envio Definitivamente el Correo */
                resultado = emailService.sendEmail(email);

                if (resultado.success()) {
                                /* Si se cumplen todos los pasos, Persisto definitivamente el Token de 
                                Recuperacion */
                    this.persist(usuario.get(), true);
                    this.postPersist(usuario.get());//, null, EPersistAction.SOLRESETPAS);
                    resultado = new R(true, "Se Ha Enviado un Mail Informativo al Usuario para Iniciar el "
                                            + "Proceso de Recuperación del Password");
                }
            } catch (Exception ex) {
                resultado = new R(false, "Ha Ocurrido un Error Inesperado: " + ex.getMessage());
            }
        }

        return resultado;
    }

    @Override
    public R procesarRecuperarPassword(String token) {
        R resultado;

        Optional<Usuario> usuario = ((IUsuarioRepo) getRepo()).findByTokenResetPassword(token);

        if (usuario.isEmpty() || !usuario.get().isAlive()) {
            resultado = new R(false, C.MSJ_SEC_ERR_NOUSER_BY_TOKEN);
        } else if (!usuario.get().getEnabled()) {
            resultado = new R(false, C.MSJ_SEC_ERR_USERENABLED);
        } else if (usuario.get().getLocked()) {
            resultado = new R(false, C.MSJ_SEC_ERR_USERLOCKED);
        } else {
            try {
                /* Registro la nueva Clave */
                String clavetemporal = this.generarClaveTemporal(usuario.get());

                usuario.get().setPassword(SecurityService.encodePassword(clavetemporal));
                usuario.get().setExpired(true);
                usuario.get().setExpiresAt(F.toLocalDate(new Date()));
                usuario.get().setTokenresetpassword(null);

                //////////////////////////////////////////////////////////
                // Envio Correo de Aviso al Usuario                     //
                //////////////////////////////////////////////////////////
                /* Armo contenido del Correo */
                String contenido = "<div>"
                                   + "<h2><strong>Estimado/a "
                                   + usuario.get().getEmail()
                                   //.getPersona()
                                   //.getRazonsocial()
                                   + "</strong></h2>"
                                   + "</br>"
                                   + "<p>Su usuario para ingresar al sistema es <strong>"
                                   + usuario.get().getName()
                                   + "</strong></p>"
                                   + "</br>"
                                   + "<p>Su clave fue regenerada y temporalmente es <strong>"
                                   + clavetemporal
                                   + "</strong>"
                                   + "</p>"
                                   + "</br>"
                                   + "<p>Deberá cambiarla en el próximo ingreso."
                                   + "</p>"
                                   + "</br>"
                                   + "<p>No responda este correo electrónico, ya que fue enviado "
                                   + "automáticamente</p>"
                                   + "</br>";

                /* Preparo Datos para el Correo */
                Email email = new Email();
                email.setDestinatarios(new String[]{usuario.get().getEmail()});
                email.setConCopiaA(null);
                email.setResponderA(null);
                email.setAsunto("Se ha Procesado su Solicitud de Recuperación del Password");
                email.setContenido(contenido);

                /* Envio Definitivamente el Correo */
                resultado = emailService.sendEmail(email);

                if (resultado.success()) {
                            /* Si se cumplen todos los pasos, Persisto definitivamente el Token de 
                            Recuperacion */
                    this.persist(usuario.get(), true);
                    this.postPersist(usuario.get());//, null, EPersistAction.PRORESETPAS);
                    resultado = new R(true,
                                      "Se Ha Enviado un Mail Informativo al Usuario con la Clave Temporal " + "Generada");
                }
            } catch (Exception ex) {
                resultado = new R(false, "Ha Ocurrido un Error Inesperado: " + ex.getMessage());
            }
        }

        return resultado;
    }

    @Override
    public R registrarSolicitudCheckEmail(String username, String email) {
        R resultado;
        ITokenPayload usuarioSesion = ((Token) SecurityService.getAuthentication().getPrincipal()).getPayload();

        if (usuarioSesion != null) {
            resultado = new R(false, C.MSJ_APP_CHANGEEMAIL_ERR_NOBODY);
        } else if (!usuarioSesion.getName().equals(username)) {
            resultado = new R(false, C.MSJ_APP_CHANGEEMAIL_ERR_FOROHTER);
        } else if (email.isEmpty() || email.isBlank() || email == null) {
            resultado = new R(false, "Debe Indicar la Dirección de Correo Electrónico");
        } else {
            Optional<Usuario> usuario = ((IUsuarioRepo) getRepo()).findByUsername(username);
            if (usuario.isEmpty() || !usuario.get().isAlive()) {
                resultado = new R(false, C.MSJ_SEC_ERR_NOUSER);
            } else if (!usuario.get().getEnabled()) {
                resultado = new R(false, C.MSJ_SEC_ERR_USERENABLED);
            } else if (usuario.get().getLocked()) {
                resultado = new R(false, C.MSJ_SEC_ERR_USERLOCKED);
            } else {
                try {
                    /* Registro el Token de Confirmacion de Email */
                    String textoacodificar = usuario.get().getId().toString() + F.getNowDateAsString("");
                    String mi_token = ECodeFunction.GEN_CHECK_EMAIL.getPrefijo().concat(T.getMd5Token(textoacodificar));
                    usuario.get().setEmail(email);
                    usuario.get().setEmailchecktoken(mi_token);

                    //////////////////////////////////////////////////////////
                    // Envio Correo de Aviso al Usuario                     //
                    //////////////////////////////////////////////////////////
                    /* Armo contenido del Correo */
                    String contenido = "<div>"
                                       + "<h2><strong>Estimado/a "
                                       + usuario.get().getEmail()
                                       //.getPersona()
                                       //.getRazonsocial()
                                       + "</strong></h2>"
                                       + "</br>"
                                       + "<p>Usted ha Solicitado Iniciar el Proceso de "
                                       + "Confirmación de su Dirección de Correo Electrónico</p>"
                                       + "</br>"
                                       + "<p>para el usuario: <strong>"
                                       + usuario.get().getName()
                                       + "</strong>, Correo Electrónico: <strong>"
                                       + usuario.get().getEmail()
                                       + "</strong></p>"
                                       + "</br>"
                                       + "<p>Para Efectivizar la Confirmación utilice el "
                                       + "siguiente Link "
                                       + this.urlconsultaqr
                                       + mi_token
                                       + "</p>"
                                       + "</br>"
                                       + "<p>No responda este correo electrónico, ya que fue "
                                       + "enviado automáticamente</p>"
                                       + "</br>";

                    /* Preparo Datos para el Correo */
                    Email emailBody = new Email();
                    emailBody.setDestinatarios(new String[]{usuario.get().getEmail()});
                    emailBody.setConCopiaA(null);
                    emailBody.setResponderA(null);
                    emailBody.setAsunto("Solicitud de Confirmación de la Dirección de Correo Electrónico");
                    emailBody.setContenido(contenido);

                    /* Envio Definitivamente el Correo */
                    resultado = emailService.sendEmail(emailBody);

                    if (resultado.success()) {
                                    /* Si se cumplen todos los pasos, Persisto definitivamente el Token de 
                                    Solicitud Confirmacion */
                        this.persist(usuario.get(), true);
                        this.postPersist(usuario.get());//, null, EPersistAction.SOLCHKEMAIL);
                        resultado = new R(true, "Se Ha Enviado un Mail Informativo al Usuario para Iniciar el "
                                                + "Proceso de Confirmacion de Correo Electronico");
                    }
                } catch (Exception ex) {
                    resultado = new R(false, "Ha Ocurrido un Error Inesperado: " + ex.getMessage());
                }
            }
        }

        return resultado;
    }

    @Override
    public R procesarCheckEmail(String token) {
        R resultado;

        Optional<Usuario> usuario = ((IUsuarioRepo) getRepo()).findByTokenCheckEmail(token);

        if (usuario.isEmpty() || !usuario.get().isAlive()) {
            resultado = new R(false, C.MSJ_SEC_ERR_NOUSER_BY_TOKEN);
        } else if (!usuario.get().getEnabled()) {
            resultado = new R(false, C.MSJ_SEC_ERR_USERENABLED);
        } else if (usuario.get().getLocked()) {
            resultado = new R(false, C.MSJ_SEC_ERR_USERLOCKED);
        } else {
            try {
                /* Registro el Check del Correo Electronico */
                usuario.get().setEmailcheckAt(F.fechaActualLocalDateTime());
                usuario.get().setEmailchecktoken(null);

                //////////////////////////////////////////////////////////
                // Envio Correo de Aviso al Usuario                     //
                //////////////////////////////////////////////////////////
                /* Armo contenido del Correo */
                String contenido = "<div>"
                                   + "<h2><strong>Estimado/a "
                                   + usuario.get().getEmail()
                                   //.getPersona()
                                   //.getRazonsocial()
                                   + "</strong></h2>"
                                   + "</br>"
                                   + "<p>Su Dirección de Correo Electrónico <strong>"
                                   + usuario.get().getEmail()
                                   + "</strong> fue Verificada.</p>"
                                   + "</br>"
                                   + "<p>y asociada al usuario <strong>"
                                   + usuario.get().getName()
                                   + "</strong></p>"
                                   + "</br>"
                                   + "<p>No responda este correo electrónico, ya que fue enviado "
                                   + "automáticamente</p>"
                                   + "</br>";

                /* Preparo Datos para el Correo */
                Email email = new Email();
                email.setDestinatarios(new String[]{usuario.get().getEmail()});
                email.setConCopiaA(null);
                email.setResponderA(null);
                email.setAsunto("Se ha Verificado su Dirección de Correo Electrónico");
                email.setContenido(contenido);

                /* Envio Definitivamente el Correo */
                resultado = emailService.sendEmail(email);

                if (resultado.success()) {
                            /* Si se cumplen todos los pasos, Persisto definitivamente la Confirmacion del 
                            Corre Electronico */
                    this.persist(usuario.get(), true);
                    this.postPersist(usuario.get());//, null, EPersistAction.PROCHKEMAIL);
                    resultado = new R(true, "Se Ha Enviado un Mail Informativo al Usuario confirmando la "
                                            + "Verificacion Correcta de la Dirección de Correo Electrónico");
                }
            } catch (Exception ex) {
                resultado = new R(false, "Ha Ocurrido un Error Inesperado: " + ex.getMessage());
            }
        }

        return resultado;
    }

    @Override
    public String generarClaveTemporal(Usuario usuario) {
        if (usuario == null) {
            throw new EntityException(C.SYS_EMAIL_ERR_NOUSER, C.SYS_CAD_SEC_USUARIO);
        }

        var email = usuario.getEmail();
        if (email == null || email.isEmpty()) {
            throw new EntityException(C.SYS_EMAIL_ERR_NODIR, usuario.getName());
        }

        return usuario.getEmail().split("@")[0].toLowerCase()
                                               .concat(C.SYS_CAD_UNDERSCORE)
                                               .concat(T.cadenaAleatoria(4).toLowerCase());
    }

    public void actualizarFoto(Long usuarioId, String imagen) {
        Usuario usuario = this.findById(usuarioId)
                              .orElseThrow(() -> new ItemNotFoundException(C.SYS_CAD_SEC_USUARIO, usuarioId.toString()));
        usuario.setFoto(imagen);
        this.persist(usuario, true);
    }
}
