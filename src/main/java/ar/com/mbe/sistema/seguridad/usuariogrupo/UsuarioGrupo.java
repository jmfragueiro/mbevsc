package ar.com.mbe.sistema.seguridad.usuariogrupo;

import ar.com.mbe.base.entity.Entidad;
import ar.com.mbe.core.common.C;
import ar.com.mbe.sistema.seguridad.grupo.Grupo;
import ar.com.mbe.sistema.seguridad.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sg_user_grupo")
@SequenceGenerator(name = "seq_sg_user_grupo", sequenceName = "seq_sg_user_grupo", allocationSize = 1)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = UsuarioGrupo.class)
public class UsuarioGrupo extends Entidad {
    public static final String F_USER_GRUPO_GRUPO = "Grupo";
    public static final String F_USER_GRUPO_USUARIO = "Usuario";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sg_user_id", referencedColumnName = "id")
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_USER_GRUPO_USUARIO)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sg_grupo_id", referencedColumnName = "id")
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_USER_GRUPO_GRUPO)
    private Grupo grupo;

    public UsuarioGrupo() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
