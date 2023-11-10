package ar.com.mbe.sistema.seguridad.permiso;

import ar.com.mbe.base.entity.Entidad;
import ar.com.mbe.core.common.C;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sg_permiso")
@SequenceGenerator(name = "seq_sg_permiso", sequenceName = "seq_sg_permiso", allocationSize = 1)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Permiso.class)
public class Permiso extends Entidad {
    public static final String F_PERMISO_PERMISO = "Permiso";
    public static final String F_PERMISO_DESCRIPCION = "Descripcion";
    public static final String F_PERMISO_PATH = "Path";

    @Column(name = "sg_modulo_id", nullable = false)
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + " (Módulo)")
    private Long modulo;

    @Column(name = "descripcion", unique = true)
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_PERMISO_DESCRIPCION)
    private String descripcion;

    @Column(name = "permiso", unique = true)
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_PERMISO_PERMISO)
    private String permiso;

    @Column(name = "path")
    private Boolean path;

    public Permiso() {
    }

    public static String getfPermisoPermiso() {
        return F_PERMISO_PERMISO;
    }

    public static String getfPermisoDescripcion() {
        return F_PERMISO_DESCRIPCION;
    }

    public static String getfPermisoPath() {
        return F_PERMISO_PATH;
    }

    public Long getModulo() {
        return modulo;
    }

    public void setModulo(Long modulo) {
        this.modulo = modulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public Boolean getPath() {
        return path;
    }

    public void setPath(Boolean path) {
        this.path = path;
    }
}
