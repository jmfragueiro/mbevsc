package ar.com.mbe.sistema.seguridad.permiso;

import ar.com.mbe.base.control.Controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permisos")
public class PermisoController extends Controlador<Permiso, Long> {
    @Autowired
    public PermisoController(IPermisoService servicio) {
        super(servicio);
    }
}
