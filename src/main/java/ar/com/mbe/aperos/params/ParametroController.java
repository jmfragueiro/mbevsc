package ar.com.mbe.aperos.params;

import ar.com.mbe.base.control.Controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/params")
public class ParametroController extends Controlador<Parametro, Long> {
    @Autowired
    public ParametroController(IParametroService servicio) {
        super(servicio);
    }
}
