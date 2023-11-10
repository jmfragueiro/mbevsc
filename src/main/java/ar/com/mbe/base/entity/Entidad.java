package ar.com.mbe.base.entity;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.F;
import ar.com.mbe.core.security.SecurityService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Esta clase abstracta representa el concepto de una Entidad que posee mecanismos que
 * permiten establecer una cierta trazabilidad de los cambios realizados sobre la misma.
 * Para ello se incorporan los atributos de fechas de alta, ultima modificacion y baja.
 * Se espera que toda entidad persistente y administrada de los sistemas creados con el
 * framework sean auditables y por ello hereden e imeplementen efectivmente de esta interfaz.
 *
 * @author jmfragueiro
 * @version 20230601
 */
@MappedSuperclass
public abstract class Entidad implements IEntidad<Long>, Serializable, Cloneable {
    @Id
    private Long id;

    @Column(name = "fechaumod")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaumod;

    @Column(name = "userumod")
    private String userumod;

    @Column(name = "fechabaja")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechabaja;

    @PrePersist
    public void onPersist() {
        userumod = SecurityService.getAuthentication().getName();
        fechaumod = F.ahoraLocalDateTime();
    }

    @PreUpdate
    public void onUpdate() {
        userumod = SecurityService.getAuthentication().getName();
        fechaumod = F.ahoraLocalDateTime();
    }

    @Override
    public boolean isNew() {
        return (id == null);
    }

    @Override
    public void kill() {
        fechabaja = F.ahoraLocalDateTime();
    }

    @Override
    public boolean isAlive() {
        return (fechabaja == null);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public LocalDateTime getFechaumod() {
        return fechaumod;
    }

    @Override
    public String getUserUmod() {
        return userumod;
    }

    @Override
    public LocalDateTime getFechabaja() {
        return fechabaja;
    }

    @Override
    public boolean equals(Object other) {
        return ((id != null) && (this == other || ((other instanceof Entidad) && id.equals(((Entidad) other).getId()))));
    }

    @Override
    public int hashCode() {
        return (43 * 5 + (getId() == null ? 0 : getId().hashCode()));
    }

    @Override
    public String toString() {
        return C.SYS_CAD_OPENTYPE.concat(getClass().getSimpleName())
                                 .concat(C.SYS_CAD_REFER)
                                 .concat(isNew() ? C.SYS_CAD_NEW : getId().toString())
                                 .concat(C.SYS_CAD_CLOSETPE);
    }
}
