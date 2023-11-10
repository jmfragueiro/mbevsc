package ar.com.mbe.base.filter;

import ar.com.mbe.base.entity.IEntidad;
import ar.com.mbe.base.service.IServicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomeFilter {
    <T extends IEntidad<K>, K> Page<T> filter(String filtro, Pageable pag, IServicio<T, K> svc) throws ClassNotFoundException;
}
