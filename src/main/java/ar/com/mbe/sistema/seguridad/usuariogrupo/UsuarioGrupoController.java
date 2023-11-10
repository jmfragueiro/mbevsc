package ar.com.mbe.sistema.seguridad.usuariogrupo;

import ar.com.mbe.base.control.Controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuariogrupos")
public class UsuarioGrupoController extends Controlador<UsuarioGrupo, Long> {
    @Autowired
    public UsuarioGrupoController(IUsuarioGrupoService servicio) {
        super(servicio);
    }
}
