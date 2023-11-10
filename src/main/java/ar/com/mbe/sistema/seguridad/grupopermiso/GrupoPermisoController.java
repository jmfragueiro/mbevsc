package ar.com.mbe.sistema.seguridad.grupopermiso;

import ar.com.mbe.base.control.Controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/grupopermisos")
public class GrupoPermisoController extends Controlador<GrupoPermiso, Long> {
    @Autowired
    public GrupoPermisoController(IGrupoPermisoService servicio) {
        super(servicio);
    }
}
