package ar.com.mbe.sistema.seguridad.usuario;

import ar.com.mbe.base.control.Controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuariosvista")
public class UsuariovistaController extends Controlador<Usuariovista, Long> {
    @Autowired
    public UsuariovistaController(IUsuariovistaService servicio) {
        super(servicio);
    }
}
