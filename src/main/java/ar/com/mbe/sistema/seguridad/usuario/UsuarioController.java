package ar.com.mbe.sistema.seguridad.usuario;

import ar.com.mbe.aperos.email.Email;
import ar.com.mbe.aperos.email.EmailService;
import ar.com.mbe.base.control.Controlador;
import ar.com.mbe.base.repos.ItemNotFoundException;
import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.F;
import ar.com.mbe.core.common.R;
import ar.com.mbe.core.exception.SecurityException;
import ar.com.mbe.core.security.SecurityService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController extends Controlador<Usuario, Long> {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private EmailService emailService;

    @Autowired
    public UsuarioController(IUsuarioService servicio) {
        super(servicio);
    }

    @GetMapping(path = "/ref/{key}", produces = "application/json")
    public ResponseEntity<Usuario> viewPorUsernameOrEmail(@PathVariable("key") String key) {
        var item = ((IUsuarioService) getServicio()).findByUsernameOrEmail(key.toLowerCase())
                                                    .orElseThrow(() -> new ItemNotFoundException(
                                                            this.getServicio().getEntityClass().getName(), key));
        return ResponseEntity.of(Optional.of(item));
    }

    @PutMapping(path = "/{key}/chpass")
    public ResponseEntity<Object> updatePASS(@PathVariable("key") String key,
                                             @RequestParam(required = false) String oldPassword,
                                             @Valid @Size(min = 3) @RequestParam String newPassword,
                                             @RequestParam String metodo) {
        try {
            R resultado;
            Usuario usuario = ((IUsuarioService) getServicio()).findByName(key)
                                                               .orElseThrow(() -> new ItemNotFoundException(
                                                                       this.getServicio().getEntityClass().getSimpleName(), key));

            /**
             * En Este IF se pregunta indistintamente la igualdad de oldPassword y usuario.getPassword
             * usando passwordEncoder y no usandolo ya que en el caso de recibir la peticion para realizar
             * un cambio por perfil de usuario, este último deberá ingresarla y este método la recibirá sin
             * encriptar,
             * la otra alternativa es cuando se resetea la password del usuario por algun motivo y se le 
             * envía una por
             * defecto, en este caso al ingresar por primera vez se le solicita en cambio, procedimiento 
             * que no vuelve
             * a solicitar la clave vieja ya que fue ingresada para llegar al cambio definitivo, en esta 
             * oportunidad
             * este método recibe la clave vieja pero encriptada que la obtiene del objeto usuario en el 
             * front.
             */
            if (oldPassword == null
                || oldPassword.isEmpty()
                || SecurityService.passwordsMatch(oldPassword, usuario.getCredential())
                || oldPassword.equals(usuario.getCredential())) {
                usuario.setPassword(SecurityService.encodePassword(newPassword));

                if (metodo.equals("RESET") || metodo.equals("ALTAUSUARIO")) {
                    usuario.setExpired(true);
                    usuario.setExpiresAt(F.toLocalDate(new Date()));
                } else {
                    usuario.setExpired(false);
                    usuario.setExpiresAt(null);
                }

                getServicio().persist(usuario, true);

                if (metodo.equals("RESET") || metodo.equals("ALTAUSUARIO")) {
                    /**
                     * Envio Mail de Aviso
                     */
                    /* Armo contenido del Correo */
                    String contenido = "";
                    String asunto = "";
                    if (metodo.equals("RESET")) {
                        asunto = "Blanqueo de Clave de Ingreso al Sistema de Géstion";
                        contenido = "<div>"
                                    + "<h2><strong>Estimado/a "
                                    + usuario.getEmail()
                                    //.getPersona().getRazonsocial()
                                    + "</strong></h2>"
                                    + "</br>"
                                    + "<p>Su usuario para ingresar al sistema es <strong>"
                                    + usuario.getName()
                                    + "</strong>.</p>"
                                    + "</br>"
                                    + "<p>Su clave fue regenerada a petición del Administrador del Sistema "
                                    + "y temporalmente es <strong>"
                                    + newPassword
                                    + "</strong></p>"
                                    + "</br>"
                                    + "<p>Deberá cambiarla en el próximo ingreso.</p>"
                                    + "</br>"
                                    + "<p>No responda este correo electrónico, ya que fue enviado "
                                    + "automáticamente</p>"
                                    + "</br>";
                    } else {
                        asunto = "Registro de Nuevo Usuario " + usuario.getName();
                        contenido = "<div>"
                                    + "<h2><strong>Bienvenido/a "
                                    + usuario.getEmail()
                                    //.getPersona().getRazonsocial()
                                    + "</strong></h2>"
                                    + "</br>"
                                    + "<p>Su usuario para ingresar al sistema es <strong>"
                                    + usuario.getName()
                                    + "</strong>.</p>"
                                    + "</br>"
                                    + "<p>Su clave fue temporal es <strong>"
                                    + newPassword
                                    + "</strong></p>"
                                    + "</br>"
                                    + "<p>Deberá cambiarla en el próximo ingreso.</p>"
                                    + "</br>"
                                    + "<p>No responda este correo electrónico, ya que fue enviado "
                                    + "automáticamente</p>"
                                    + "</br>";
                    }

                    /* Preparo Datos para el Correo */
                    Email email = new Email();
                    email.setDestinatarios(new String[]{usuario.getEmail()});
                    email.setConCopiaA(null);
                    email.setResponderA(null);
                    email.setAsunto(asunto);
                    email.setContenido(contenido);

                    /* Envio Definitivamente el Correo */
                    resultado = emailService.sendEmail(email);

                    if (resultado.success()) {
                        /* Si se cumplen todos los pasos, Persisto definitivamente el Token de Solicitud 
                        Confirmacion */
                        getServicio().persist(usuario, true);
                        resultado = new R(true, "Se Ha Enviado un Mail Informativo al Usuario para Iniciar el Proceso de "
                                                + "Confirmacion de Correo Electronico");
                    }
                }

                /**
                 * Audito el Cambio de Password
                 */
                //                if (metodo.equals("RESET")) {
                //                    getServicio().postPersist(usuario);//, null, CHPASSRESET);
                //                } else {
                //                    getServicio().postPersist(usuario);//, null, CHPASS);
                //                }

                return ResponseEntity.ok().build();
            } else {
                throw new SecurityException(C.MSJ_APP_CHPASS_ERR_ONCHANGE, C.SYS_APP_CHPASS_ERR_BADPASS);
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                                 .body(new HashMap<>().put(C.SYS_APP_TXTLOGIN_PASS.toUpperCase(), ex.getMessage()));
        }
    }

    @PutMapping(path = "/{key}/chemail")
    public ResponseEntity<Object> updateEMAIL(@PathVariable("key") String key,
                                              @RequestParam(required = false) String oldPassword,
                                              @Valid @Size(min = 4, max = 255) @RequestParam String email) {
        try {
            /* Obtengo Usuario Original y lo saco del entity manager, ya que es requerido en la Auditoria */
            Usuario usuarioOriginal = ((IUsuarioService) getServicio()).findByName(key)
                                                                       .orElseThrow(() -> new ItemNotFoundException(
                                                                               this.getServicio().getEntityClass().getSimpleName(), key));
            entityManager.detach(usuarioOriginal);

            Usuario usuario = ((IUsuarioService) getServicio()).findByName(key)
                                                               .orElseThrow(() -> new ItemNotFoundException(
                                                                       this.getServicio().getEntityClass().getSimpleName(), key));

            if (SecurityService.passwordsMatch(oldPassword, usuario.getCredential())) {
                usuario.setEmail(email);
                getServicio().persist(usuario, true);
                //getServicio().postPersist(usuario);//, usuarioOriginal, CHMAIL);
                return ResponseEntity.accepted().build();
            } else {
                throw new SecurityException(C.MSJ_APP_CHANGEEMAIL_ERR_ONCHANGE, C.SYS_APP_CHANGEEMAIL_ERR_BADEMAIL);
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new HashMap<>().put(C.SYS_APP_TXTLOGIN_EMAIL, ex.getMessage()));
        }
    }

    @PostMapping(path = "/{key}/{key1}/solicitacheckemail")
    public ResponseEntity<R> registrarSolicitudCheckEmail(@PathVariable("key") String username,
                                                          @PathVariable("key1") String email) {
        R resultado = ((IUsuarioService) getServicio()).registrarSolicitudCheckEmail(username, email);

        if (resultado.success()) {
            return ResponseEntity.ok().body(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }
}
