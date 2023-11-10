package ar.com.mbe.base.control;

import ar.com.mbe.base.entity.IEntidad;
import ar.com.mbe.base.repos.ItemNotFoundException;
import ar.com.mbe.base.service.IServicio;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación de interfase IControlador para un sistema con REST-JPA. Esta clase es
 * ademas la base esperada de controladores REST para el sistema en el framework ad-hoc.
 *
 * @param <T> El tipo de la entidad servida por el servicio
 * @param <K> El tipo de la clave de identificacion para la entidad
 * @author jmfragueiro
 * @version 20230601
 */
public abstract class Controlador<T extends IEntidad<K>, K> implements IControlador<T, K> {
    private final IServicio<T, K> servicio;

    protected Controlador(IServicio<T, K> servicio) {
        this.servicio = servicio;
    }

    protected final Map<String, Object> getErrorMessageMap(BindingResult result) {
        Map<String, Object> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
        return errores;
    }

    @Override
    public IServicio<T, K> getServicio() {
        return servicio;
    }

    @GetMapping(produces = "application/json")
    @Override
    public ResponseEntity<List<T>> list() {
        var list = this.getServicio().findAlive();

        return !list.isEmpty() ? ResponseEntity.of(Optional.of(list)) : ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/fltpg", produces = "application/json")
    @Override
    public ResponseEntity<Page<T>> filter(@RequestParam(defaultValue = "") String flt, Pageable pag)
            throws ClassNotFoundException {
        var page = this.getServicio().filter(flt, pag);

        return (page != null) ? ResponseEntity.of(Optional.of(page)) : ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{key}", produces = "application/json")
    @Override
    public ResponseEntity<T> get(@PathVariable("key") K key) {
        var item = this.getServicio()
                       .findById(key)
                       .orElseThrow(() -> new ItemNotFoundException(this.getServicio().getEntityClass().getClass().getName(),
                                                                    key.toString()));

        return (item != null) ? ResponseEntity.of(Optional.of(item)) : ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = "application/json")
    @Override
    public ResponseEntity<Object> add(@Valid @RequestBody T object, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessageMap(result));
        } else {
            T added = getServicio().persist(object, true);
            URI location = MvcUriComponentsBuilder.fromController(getClass())
                                                  .path("/{id}")
                                                  .buildAndExpand(added.getId())
                                                  .toUri();
            return ResponseEntity.created(location).body(added);
        }
    }

    @PutMapping(consumes = "application/json")
    @Override
    public ResponseEntity<Object> update(@Valid @RequestBody T object, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessageMap(result));
        } else {
            T updated = getServicio().persist(object, true);
            return ResponseEntity.accepted().body(updated);
        }
    }

    @DeleteMapping(path = "/{key}")
    @Override
    public ResponseEntity<Object> delete(@PathVariable("key") K key) {
        getServicio().findById(key).ifPresent(e -> getServicio().delete(e, true));
        return ResponseEntity.ok().build();
    }
}
