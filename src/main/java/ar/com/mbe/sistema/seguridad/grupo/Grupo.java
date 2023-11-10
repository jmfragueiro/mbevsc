package ar.com.mbe.sistema.seguridad.grupo;

import ar.com.mbe.base.entity.Entidad;
import ar.com.mbe.core.common.C;
import ar.com.mbe.sistema.seguridad.grupopermiso.GrupoPermiso;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sg_grupo")
@SequenceGenerator(name = "seq_sg_grupo", sequenceName = "seq_sg_grupo", allocationSize = 1)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id",
                  scope = Grupo.class)
public class Grupo extends Entidad {
    public static final String F_PERMISO_NAME = "Name";
    public static final String F_PERMISO_ROLES = "Roles";

    @Column(name = "name", unique = true)
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_PERMISO_NAME)
    private String name;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<GrupoPermiso> permisos = new HashSet<>();

    public Grupo() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GrupoPermiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(Set<GrupoPermiso> permisos) {
        this.permisos = permisos;
    }
}
