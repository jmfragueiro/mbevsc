package ar.com.mbe.sistema.seguridad.grupo;

import ar.com.mbe.base.control.Controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/grupos")
public class GrupoController extends Controlador<Grupo, Long> {
    @Autowired
    public GrupoController(IGrupoService servicio) {
        super(servicio);
    }
}
