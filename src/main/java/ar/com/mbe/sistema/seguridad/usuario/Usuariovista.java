package ar.com.mbe.sistema.seguridad.usuario;

import ar.com.mbe.base.entity.Entidad;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "view_sg_usuario")
public class Usuariovista extends Entidad {
    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "emailcheck_at")
    private LocalDateTime emailcheckAt;

    @Column(name = "emailchecktoken")
    private String emailchecktoken;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "foto")
    private String foto;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "persona")
    private Long persona;

    @Column(name = "documento")
    private Integer documento;

    @Column(name = "cuit")
    private String cuit;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "razonsocial")
    private String razonsocial;

    @Column(name = "genero")
    private String genero;

    @Column(name = "fechanacimiento")
    private LocalDateTime fechanacimiento;

    @Column(name = "imagen")
    private String imagen;

    public Usuariovista() { }

    public String getUsername() {
        return username;
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

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
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

    public Long getPersona() {
        return persona;
    }

    public void setPersona(Long persona) {
        this.persona = persona;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public LocalDateTime getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(LocalDateTime fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
