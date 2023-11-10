package ar.com.mbe.aperos.email;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.R;
import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Service
public class EmailService {
    @Value("${sys.app.email.tos}")
    private String destinos;

    @Value("${sys.app.email.dirimgcab}")
    private String dirImgCabecera;

    @Value("${sys.app.email.imgcab}")
    private String imgCabecera;

    @Value("${front.frontendhost}")
    private String frontendhost;

    @Value("${front.codigo}")
    private String empresacodigo;

    @Value("${front.nombre}")
    private String empresanombre;

    @Value("${front.sistema}")
    private String sistemanombre;

    private final JavaMailSender sender = new JavaMailSenderImpl();

    public R sendEmail(Email email) throws MessagingException {
        // Si estoy en Desarrollo hago que todas las pruebas
        // lleguen a una direccion de correo controlada!!!!!
        if (!this.destinos.equals(C.SYS_CAD_ALL)) {
            email.setDestinatarios(this.destinos.split(C.BUS_SEPARADOR_PARES));
        }

        return sendEmailTool(email);
    }

    private R sendEmailTool(Email email) throws MessagingException {
        R resultado;

        String[] destinatario = email.getDestinatarios();
        String[] concopia = (email.getConCopiaA() != null ? email.getConCopiaA() : new String[]{});
        Collection<EmailMultiPart> multipartes =
                (email.getMultiparts() != null ? email.getMultiparts() : new ArrayList<>());
        String subject = email.getAsunto();
        String textMessage = this.getPlantillaBase().get(C.SYS_EMAIL_CFG_PLANTILLA_HEAD)
                             + email.getContenido()
                             + this.getPlantillaBase().get(C.SYS_EMAIL_CFG_PLANTILLA_FOOT);
        String responder = email.getResponderA();

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        try {
            if (destinatario.length > 0) {
                if (!textMessage.isEmpty()) {
                    if (!subject.isEmpty()) {

                        /* Defino los Destinatarios del Mail */
                        helper.setTo(destinatario);
                        if (concopia.length > 0) {
                            helper.setCc(concopia);
                        }

                        /* Defino la Direccion Predefinida para Responder el Mail  */
                        if (responder != null && !responder.isEmpty()) {
                            helper.setReplyTo(responder);
                        }

                        /* Defino el Asunto del Mail */
                        helper.setSubject(subject);

                        /* Defino el Objeto Multipart que utilizo para definir el contenido y los adjuntos
                        del mail */
                        Multipart multiPart = new MimeMultipart(C.SYS_EMAIL_CFG_MIMESUBTYPE);

                        /* Defino el Contenido del Mail (Cuerpo Principal) y lo registro en multipart */
                        //helper.setText(textMessage, true); esta era la primera version (mas simple) pero
                        // no tengo la opcion de multipart
                        MimeBodyPart textPart = new MimeBodyPart();
                        textPart.setContent(textMessage, C.SYS_EMAIL_CFG_CONTENT_TYPE);
                        multiPart.addBodyPart(textPart);

                        /* Proceso los Archivos que seran parte del Cuerpo definiendo los como Multipart */
                        multipartes.forEach(mp -> {
                            try {
                                ByteArrayDataSource imageDataSource =
                                        new ByteArrayDataSource(mp.getArchivo(), mp.getMimetype());
                                BodyPart imagePart = new MimeBodyPart();
                                imagePart.setDataHandler(new DataHandler(imageDataSource));
                                imagePart.setHeader(C.SYS_EMAIL_CFG_CONTENT_ID, C.SYS_CAD_OPENREF
                                                                                + mp.getReferencia()
                                                                                + C.SYS_CAD_CLOSEREF);
                                imagePart.setFileName(mp.getNombre());

                                multiPart.addBodyPart(imagePart);
                                message.setContent(multiPart);

                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        });
                        message.setContent(multiPart);

                        /* Finalmente envio el mail */
                        sender.send(message);
                        resultado = new R(true, C.SYS_EMAIL_INF_OK);
                    } else {
                        resultado = new R(false, C.SYS_EMAIL_ERR_NOSUBJECT);
                    }
                } else {
                    resultado = new R(false, C.SYS_EMAIL_ERR_NOMESSAGE);
                }
            } else {
                resultado = new R(false, C.SYS_EMAIL_ERR_NOTO);
            }
        } catch (MessagingException e) {
            resultado = new R(false,C.SYS_EMAIL_ERR_FATAL + C.SYS_CAD_LOGSEP + C.SYS_CAD_SPACE + e);
        }

        return resultado;
    }

    public HashMap<String, String> getPlantillaBase() {
        HashMap<String, String> plantilla = new HashMap<>();

        plantilla.put(C.SYS_EMAIL_CFG_PLANTILLA_HEAD,
                      "<table id=\"\" style=\"width: 90%; color: #666666; height: 220px; font-family: "
                      + "Arial;\" "
                      + "cellspacing=\"0\" align=\"center\">"
                      + "<tbody>"
                      + "<tr>"
                      + "<td style=\"padding:20px 0px; font-size: 20px; text-align: left;\">"
                      + "<img class=\"alignnone\" src=\""
                      + this.frontendhost
                      + this.dirImgCabecera
                      + this.empresacodigo
                      + this.imgCabecera
                      + "\" height=\"50\" />"
                      + "</td>"
                      + "</tr>"
                      + "<tr>");

        // aca en el medio se genera el contenido del mensaje dentro del tr

        plantilla.put(C.SYS_EMAIL_CFG_PLANTILLA_FOOT, "</tr>"
                                                      + "<tr>"
                                                      + "<div class=\"row\">"
                                                      + "<div class=\"<col-12\""
                                                      + "<div style=\"text-align: center; color: red;"
                                                      + "\"><p>No responda este correo "
                                                      + "electrónico, ya que fue enviado "
                                                      + "automáticamente</p></div>"
                                                      + "</div>"
                                                      + "</div>"
                                                      + "</tr>"
                                                      + "<tr>"
                                                      + "<td style=\"padding-top: 5px;\" align=\"center\">"
                                                      + "<ul style=\"list-style: none; text-align: center;\">"
                                                      + "<li style=\"display: inline-block;\"><strong>"
                                                      + this.sistemanombre
                                                      + "</strong></li>"
                                                      + "<li style=\"display: inline-block; margin:0px "
                                                      + "10px;\">|</li>"
                                                      + "<li style=\"display: inline-block;\"><strong>"
                                                      + this.empresanombre
                                                      + "</strong></li>"
                                                      + "</ul>"
                                                      + "</td>"
                                                      + "</tr>"
                                                      + "</tbody>"
                                                      + "</table>");

        return plantilla;
    }
}