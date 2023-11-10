package ar.com.mbe.aperos.codes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
public class CodeService {
    private List<ICodeClass> codeClasses;

    @Autowired
    public CodeService(List<ICodeClass> codeClasses) {
        this.codeClasses = codeClasses;
    }

    /**
     * Este metodo gennera u codigo QR a partir de los datos pasados como argumento.
     *
     * @param text   el texto a representar en el codigo QR
     * @param width  ancho del codigo qr
     * @param height alto del codigo qr
     * @return la cadena de bytes que expresa la imagen del codigo QR generado
     */
    public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        return pngData;
    }

    /**
     * Este metodo gennera un codigo de barra "3 de 9" a partir de los datos pasados como argumento.
     *
     * @param text   el texto a representar en el codigo de barra
     * @param width  ancho del codigo de barra
     * @param height alto del codigo de barra
     * @return la cadena de bytes que expresa la imagen del  codigo de barra generado
     */
    public static byte[] getBARCODE39Image(String text, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new Code39Writer().encode(text, BarcodeFormat.CODE_39, width, height, null);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        return pngData;
    }

    /**
     * Este metodo devuelve datos de alguna clase, de acuerdo a la informacion que venga en el
     * token. Cada clase debe interpretar el token para saber si tiene que entrar en accion,
     * consiguiendo y retornando los datos requeridos, o no.
     * (ejemplo: que los 4 primeros caracteres del token recibido identifica el origen de datos
     * y, por ello, le permite a la clase saber si debe actuar -isThatCodeClass = true- o dejar
     * pasar -isThatCodeClass = false-).
     * NOTA: Solo se devuelven los datos obtenidos por la primer clase que pueda responder a la
     * consulta (ordenadas por PRIORIDAD).
     *
     * @param token el token con los datos origen de la busqueda
     * @return el objeto, del tipo apropiado, que resulta de la busqueda
     */
    public Object getDatosByToken(String token) {
        return codeClasses.stream()
                          .filter(q -> q.isThatCodeClass(token))
                          .min(Comparator.comparing(ICodeClass::getPrioridad))
                          .map(qrc -> qrc.findByToken(token));
    }
}
