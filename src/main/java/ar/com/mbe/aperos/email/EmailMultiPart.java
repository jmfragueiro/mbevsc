package ar.com.mbe.aperos.email;

public class EmailMultiPart {
    private String nombre;
    private String mimetype;
    private String referencia;
    private byte[] archivo;

    public EmailMultiPart() { }

    public EmailMultiPart(String nombre, String mimetype, String referencia, byte[] archivo) {
        this.nombre = nombre;
        this.mimetype = mimetype;
        this.referencia = referencia;
        this.archivo = archivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }
}