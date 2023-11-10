package ar.com.mbe.sistema.seguridad.usuario;

import ar.com.mbe.base.entity.Entidad;
import ar.com.mbe.core.common.C;
import ar.com.mbe.core.token.ITokenPayload;
import ar.com.mbe.sistema.seguridad.grupo.Grupo;
import ar.com.mbe.sistema.seguridad.grupopermiso.GrupoPermiso;
import ar.com.mbe.sistema.seguridad.usuariogrupo.UsuarioGrupo;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "sg_user")
@SequenceGenerator(name = "seq_sg_user", sequenceName = "seq_sg_user", allocationSize = 1)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id",
                  scope = Usuario.class)
public class Usuario extends Entidad implements ITokenPayload {
    public static final String F_USR_USUARIO = "Usuario";
    public static final String F_USR_PASSWORD = "Password";
    public static final String F_USR_NOMBRE = "Nombre";
    public static final String F_USR_EMAIL = "Email";

    @Column(name = "username", unique = true)
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_USR_USUARIO)
    @Size(min = 4, max = 255, message = C.MSJ_ERR_DB_FIELD_LONGNOK + F_USR_USUARIO)
    private String username;

    @Column(name = "email", unique = true)
    @Size(min = 4, max = 255, message = C.MSJ_ERR_DB_FIELD_LONGNOK + F_USR_EMAIL)
    @Pattern(regexp = ".+@.+\\.[a-z]+", message = C.MSJ_ERR_DB_FIELD_NOK + F_USR_EMAIL)
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_USR_EMAIL)
    private String email;

    private LocalDateTime emailcheckAt;

    private String emailchecktoken;

    private String password;

    private String foto;

    private Boolean enabled;

    private LocalDateTime lastLogin;

    private Boolean locked;

    private Boolean expired;

    private LocalDate expiresAt;

    private String tokenresetpassword;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@OrderBy("grupo")
    private Set<UsuarioGrupo> grupos = new HashSet<>();

    @Transient
    private Collection<? extends GrantedAuthority> auths;

    public Usuario() { }

    public String getUsername() {
        return username;
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public String getCredential() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> reinitAuthorities() {
        this.auths = getGrupos().stream()
                                .map(UsuarioGrupo::getGrupo)
                                .map(Grupo::getPermisos)
                                .flatMap(Collection::stream)
                                .map(GrupoPermiso::getPermiso)
                                .map(p -> new SimpleGrantedAuthority(p.getPermiso()))
                                .collect(Collectors.toList());

        return this.auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (auths == null) ? this.reinitAuthorities() : this.auths;
    }

    @Override
    public boolean isNonExpired() {
        return (expired == null || !expired);
    }

    @Override
    public boolean isNonLocked() {
        return (locked == null || !locked);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getEmailcheckAt() {
        return emailcheckAt;
    }

    public void setEmailcheckAt(LocalDateTime emailcheckAt) {
        this.emailcheckAt = emailcheckAt;
    }

    public String getEmailchecktoken() {
        return emailchecktoken;
    }

    public void setEmailchecktoken(String emailchecktoken) {
        this.emailchecktoken = emailchecktoken;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDate expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getTokenresetpassword() {
        return tokenresetpassword;
    }

    public void setTokenresetpassword(String tokenresetpassword) {
        this.tokenresetpassword = tokenresetpassword;
    }

    public Set<UsuarioGrupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(Set<UsuarioGrupo> grupos) {
        this.grupos = grupos;
    }

    public void onLogin() {
        setLastLogin(LocalDateTime.now());
    }
}
