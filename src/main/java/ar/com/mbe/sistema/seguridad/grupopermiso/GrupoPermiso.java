package ar.com.mbe.sistema.seguridad.grupopermiso;

import ar.com.mbe.base.entity.Entidad;
import ar.com.mbe.core.common.C;
import ar.com.mbe.sistema.seguridad.grupo.Grupo;
import ar.com.mbe.sistema.seguridad.permiso.Permiso;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sg_grupo_permiso")
@SequenceGenerator(name = "seq_sg_grupo_permiso", sequenceName = "seq_sg_grupo_permiso", allocationSize = 1)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = GrupoPermiso.class)
public class GrupoPermiso extends Entidad {
    public static final String F_GRUPO_PERMISO_GRUPO = "Grupo";
    public static final String F_GRUPO_PERMISO_PERSONA = "Persona";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sg_grupo_id", referencedColumnName = "id")
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_GRUPO_PERMISO_GRUPO)
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sg_permiso_id", referencedColumnName = "id")
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_GRUPO_PERMISO_PERSONA)
    private Permiso permiso;

    public GrupoPermiso() { }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }
}
