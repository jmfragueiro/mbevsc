package ar.com.mbe.base.filter;

import ar.com.mbe.base.entity.IEntidad;
import ar.com.mbe.base.service.IServicio;
import ar.com.mbe.core.common.C;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomFilter implements ICustomeFilter {
    @Override
    public <T extends IEntidad<K>, K> Page<T> filter(String filtro, Pageable pag, IServicio<T, K> svc)
            throws ClassNotFoundException {
        EntityManager em = svc.getEM();
        Class<T> clase = svc.getEntityClass();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clase);
        CriteriaQuery<Long> count = cb.createQuery(Long.class);
        Root<T> root = query.from(clase);
        Root<T> rootcount = count.from(clase);

        // depuro la cadena de filtro por las condiciones
        var conditions = this.getConditions(filtro, svc);

        // obtengo los listados de predicados (como array)
        var predicates = this.getPredicates(conditions, cb, root).toArray(new Predicate[0]);
        var predicount = this.getPredicates(conditions, cb, rootcount).toArray(new Predicate[0]);

        // obtengo el orden de la consulta
        var orders = this.getOrders(pag, cb, root);

        // configuro la consulta real completa
        query.select(root).where(predicates).orderBy(orders);
        TypedQuery<T> result = em.createQuery(query);

        // configuro la consulta para la paginacion
        count.select(cb.count(rootcount)).where(predicount);
        TypedQuery<Long> rescnt = em.createQuery(count);

        // configuro los valores a obtener según la paginación
        result.setFirstResult(pag.getPageNumber() * pag.getPageSize());
        result.setMaxResults(pag.getPageSize());

        // retorna la pagina resultado
        /////////////////////////////////////////////////////////////////////////////////////
        // Ojo!!!!!!!!! aca hay un bug de Hibernate que se está trabajando todavia!!!!!!!  //
        /////////////////////////////////////////////////////////////////////////////////////
        return new PageImpl<>(result.getResultList(), pag, rescnt.getSingleResult());
    }

    private <T extends IEntidad<K>, K> List<RPredicado> getConditions(String filtro, IServicio<T, K> svc) {
        return Arrays.stream(filtro.split(C.BUS_SEPARADOR_PARES))
                     .map(p -> new RPredicado(p.split(C.BUS_SEPARADOR_CLAVE_VALOR)[0],
                                              svc.getPredicadoConfig(p.split(C.BUS_SEPARADOR_CLAVE_VALOR)[0]),
                                              p.split(C.BUS_SEPARADOR_CLAVE_VALOR)[1]))
                     .collect(Collectors.toList());
    }

    private <T extends IEntidad<K>, K> List<Predicate> getPredicates(List<RPredicado> conditions, CriteriaBuilder cb,
                                                                     Root<T> root) {
        // Establezco las Condiciones por defecto de la Consulta
        List<Predicate> predicates = new ArrayList<>();

        // establezco las condiciones propias de la consulta
        conditions.forEach(p -> {
            switch (p.getOp()) {
                case IS_NULL -> predicates.add(cb.isNull(root.get(p.field())));
                case IS_NOTNULL -> predicates.add(cb.isNotNull(root.get(p.field())));
                case EQUAL -> predicates.add(cb.equal(root.get(p.field()), p.getParsedValue()));
                case LIKE -> predicates.add(cb.like(root.get(p.field()), (p.isIgnoreCase()
                                                                          ? "%" + p.valor().toLowerCase() + "%"
                                                                          : "%" + p.valor() + "%")));
                case GREATER_THAN -> predicates.add(switch (p.getTipo()) {
                    case DATE -> cb.greaterThan(root.get(p.field()), (LocalDateTime) p.getParsedValue());
                    case INTEGER -> cb.greaterThan(root.get(p.field()), (Integer) p.getParsedValue());
                    case LONG -> cb.greaterThan(root.get(p.field()), (Long) p.getParsedValue());
                    default -> cb.greaterThan(root.get(p.field()), p.valor());
                });
                case LESS_THAN -> predicates.add(switch (p.getTipo()) {
                    case DATE -> cb.lessThan(root.get(p.field()), (LocalDateTime) p.getParsedValue());
                    case INTEGER -> cb.lessThan(root.get(p.field()), (Integer) p.getParsedValue());
                    case LONG -> cb.lessThan(root.get(p.field()), (Long) p.getParsedValue());
                    default -> cb.lessThan(root.get(p.field()), p.valor());
                });
                case GREATER_THAN_OR_EQ -> predicates.add(switch (p.getTipo()) {
                    case DATE -> cb.greaterThanOrEqualTo(root.get(p.field()), (LocalDateTime) p.getParsedValue());
                    case INTEGER -> cb.greaterThanOrEqualTo(root.get(p.field()), (Integer) p.getParsedValue());
                    case LONG -> cb.greaterThanOrEqualTo(root.get(p.field()), (Long) p.getParsedValue());
                    default -> cb.greaterThanOrEqualTo(root.get(p.field()), p.valor());
                });
                case LESS_THAN_OR_EQ -> predicates.add(switch (p.getTipo()) {
                    case DATE -> cb.lessThanOrEqualTo(root.get(p.field()), (LocalDateTime) p.getParsedValue());
                    case INTEGER -> cb.lessThanOrEqualTo(root.get(p.field()), (Integer) p.getParsedValue());
                    case LONG -> cb.lessThanOrEqualTo(root.get(p.field()), (Long) p.getParsedValue());
                    default -> cb.lessThanOrEqualTo(root.get(p.field()), p.valor());
                });
                case BETWEEN -> {
                    String[] pares = p.valor().split(C.BUS_SEPARADOR_BETWEEN);
                    predicates.add(switch (p.getTipo()) {
                        case DATE -> cb.greaterThan(root.get(p.field()), LocalDateTime.parse(pares[0]));
                        case INTEGER -> cb.greaterThan(root.get(p.field()), Integer.decode(pares[0]));
                        case LONG -> cb.greaterThan(root.get(p.field()), Long.valueOf(pares[0]));
                        default -> cb.greaterThan(root.get(p.field()), pares[0]);
                    });
                    predicates.add(switch (p.getTipo()) {
                        case DATE -> cb.lessThan(root.get(p.field()), LocalDateTime.parse(pares[1]));
                        case INTEGER -> cb.lessThan(root.get(p.field()), Integer.decode(pares[1]));
                        case LONG -> cb.lessThan(root.get(p.field()), Long.valueOf(pares[1]));
                        default -> cb.lessThan(root.get(p.field()), pares[1]);
                    });
                }
            }
        });

        return predicates;
    }

    private <T extends IEntidad<K>, K> List<Order> getOrders(Pageable page, CriteriaBuilder cb, Root<T> root) {
        List<Order> orders = new ArrayList<>();

        page.getSort()
            .forEach(o -> orders.add(
                    (o.isAscending() ? cb.asc(root.get(o.getProperty())) : cb.desc(root.get(o.getProperty())))));

        return orders;
    }
}
