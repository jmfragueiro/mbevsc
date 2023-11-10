package ar.com.mbe.core.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Esta clase debe ser utilizada como un punto focal para todas los metodos genericos
 * para trabajar barcodes y QR (generación, transformacion, etc) en el proto-framework,
 * de manera de tener encapsuladas, en una sola clase, todas esas cuestiones asociadas
 * tipo de necesidades.
 *
 * @author jmfragueiro
 * @version 20230601
 */
@Service
public class Q {
    public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    public static byte[] getBARCODE39Image(String text, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new Code39Writer().encode(text, BarcodeFormat.CODE_39, 150, 80, null);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }
}
