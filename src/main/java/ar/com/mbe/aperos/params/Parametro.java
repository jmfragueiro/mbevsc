package ar.com.mbe.aperos.params;

import ar.com.mbe.base.entity.Entidad;
import ar.com.mbe.core.common.C;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Esta clase representa a un parametro del sistema y contiene, ademas de un conjunto de campos para guardar
 * valores de distinto tipo: double, long, booelan, Timestamp, etConstantes. Los parámetros tiene un Tipo
 * basado en la enumeracion ETipoParametro.
 *
 * @author jmfragueiro
 * @version 20200201
 * @see EParametroTipo
 */
@Entity
@Table(name = "ut_parametro")
@SequenceGenerator(name = "id_parametro_generator", sequenceName = "seq_ut_parametro", allocationSize = 1)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Parametro.class)
public class Parametro extends Entidad {
    public static final String F_PAR_TIPO = "Tipo";
    public static final String F_PAR_BASE = "Parametro Base";
    public static final String F_PAR_ORDEN = "Orden";
    public static final String F_PAR_NOMBRE = "Nombre";
    public static final String F_PAR_VALORINT = "Valor Entero";
    public static final String F_PAR_VALORDOB = "Valor Decimal";
    public static final String F_PAR_VALORSTR = "Valor Cadena";
    public static final String F_PAR_VALORBOL = "Valor Booleano";
    public static final String F_PAR_VALORDAT = "Valor Fecha";
    public static final String F_PAR_VALORCHR = "Valor Caracter";
    public static final String F_PAR_VALORIMG = "Valor Imagen";

    @Column(name = "tipo")
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_PAR_TIPO)
    private Integer tipo;

    @Column(name = "base")
    private Long base;

    @Column(name = "orden")
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_PAR_ORDEN)
    private Integer orden;

    @Column(name = "nombre")
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_PAR_NOMBRE)
    @Size(min = 4, max = 32, message = C.MSJ_ERR_DB_FIELD_LONGNOK + F_PAR_NOMBRE)
    private String nombre;

    @Column(name = "referencia")
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_PAR_NOMBRE)
    @Size(min = 4, max = 32, message = C.MSJ_ERR_DB_FIELD_LONGNOK + F_PAR_NOMBRE)
    private String referencia;

    @Column(name = "descripcion")
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_PAR_NOMBRE)
    @Size(min = 4, max = 32, message = C.MSJ_ERR_DB_FIELD_LONGNOK + F_PAR_NOMBRE)
    private String descripcion;

    @Column(name = "valorint")
    private Long valorint;

    @Column(name = "valordob")
    private Double valordob;

    @Column(name = "valorstr")
    @Size(max = 255, message = C.MSJ_ERR_DB_FIELD_LONGNOK + F_PAR_VALORSTR)
    private String valorstr;

    @Column(name = "valorbol", columnDefinition = "boolean default false")
    @NotNull(message = C.MSJ_ERR_DB_FIELD_EMPTY + F_PAR_VALORBOL)
    private Boolean valorbol;

    @Column(name = "valordat")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime valordat;

    @Column(name = "valorchr", columnDefinition = "char", length = 1)
    private String valorchr;

    @Column(name = "valorimg")
    @Size(max = 255, message = C.MSJ_ERR_DB_FIELD_LONGNOK + F_PAR_VALORIMG)
    private String valorimg;

    public Parametro() {
        orden = 0;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Long getBase() {
        return base;
    }

    public void setBase(Long base) {
        this.base = base;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getValorint() {
        return valorint;
    }

    public void setValorint(Long valorint) {
        this.valorint = valorint;
    }

    public Double getValordob() {
        return valordob;
    }

    public void setValordob(Double valordob) {
        this.valordob = valordob;
    }

    public String getValorstr() {
        return valorstr;
    }

    public void setValorstr(String valorstr) {
        this.valorstr = valorstr;
    }

    public Boolean getValorbol() {
        return valorbol;
    }

    public void setValorbol(Boolean valorbol) {
        this.valorbol = valorbol;
    }

    public LocalDateTime getValordat() {
        return valordat;
    }

    public void setValordat(LocalDateTime valordat) {
        this.valordat = valordat;
    }

    public String getValorchr() {
        return valorchr;
    }

    public void setValorchr(String valorchr) {
        this.valorchr = valorchr;
    }

    public String getValorimg() {
        return valorimg;
    }

    public void setValorimg(String valorimg) {
        this.valorimg = valorimg;
    }

    @Override
    public String toString() {
        return (isNew() ? super.toString() : nombre);
    }
}
