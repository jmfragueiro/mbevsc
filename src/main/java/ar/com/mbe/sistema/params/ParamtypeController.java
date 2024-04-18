package ar.com.mbe.sistema.params;

import ar.com.mbe.base.control.Controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/partyp")
public class ParamtypeController extends Controlador<Parametro, Long> {
    @Autowired
    public ParamtypeController(IParametroService servicio) {
        super(servicio);
    }

    @GetMapping(produces = "application/json")
    @Override
    public ResponseEntity<List<Parametro>> list() {
        var list = ((IParametroService)this.getServicio()).findByTipoOrderByOrden(0);

        return !list.isEmpty() ? ResponseEntity.of(Optional.of(list)) : ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{key}", produces = "application/json")
    @Override
    public ResponseEntity<Parametro> get(@PathVariable("key") Long key) {
        return this.get(0, key.intValue());
    }

    @GetMapping(path = "/{type}/{ord}", produces = "application/json")
    public ResponseEntity<Parametro> get(@PathVariable("type") Integer type, @PathVariable("ord") Integer ord) {
        var item = ((IParametroService)this.getServicio()).findByTipoAndOrden(type, ord);

        return item.isPresent() ? ResponseEntity.of(item) : ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/bybas/{type}/{base}", produces = "application/json")
    public ResponseEntity<List<Parametro>> get(@PathVariable("type") Integer type, @PathVariable("base") Long base) {
        var list = ((IParametroService)this.getServicio()).findByTipoAndBaseOrderByOrden(type, base);

        return !list.isEmpty() ? ResponseEntity.of(Optional.of(list)) : ResponseEntity.noContent().build();
    }
}
