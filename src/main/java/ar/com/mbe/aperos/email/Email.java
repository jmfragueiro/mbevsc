package ar.com.mbe.aperos.email;

import java.util.Collection;

public class Email {
    private String[] destinatarios;
    private String[] conCopiaA;
    private String responderA;
    private String asunto;
    private String contenido;
    private Collection<EmailMultiPart> multiparts;

    public Email() { }

    public Email(String[] destinatarios,
                 String[] conCopiaA,
                 String responderA,
                 String asunto,
                 String contenido,
                 Collection<EmailMultiPart> multiparts) {
        this.destinatarios = destinatarios;
        this.conCopiaA = conCopiaA;
        this.responderA = responderA;
        this.asunto = asunto;
        this.contenido = contenido;
        this.multiparts = multiparts;
    }

    public String[] getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String[] destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String[] getConCopiaA() {
        return conCopiaA;
    }

    public void setConCopiaA(String[] conCopiaA) {
        this.conCopiaA = conCopiaA;
    }

    public String getResponderA() {
        return responderA;
    }

    public void setResponderA(String responderA) {
        this.responderA = responderA;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Collection<EmailMultiPart> getMultiparts() {
        return multiparts;
    }

    public void setMultiparts(Collection<EmailMultiPart> multiparts) {
        this.multiparts = multiparts;
    }
}