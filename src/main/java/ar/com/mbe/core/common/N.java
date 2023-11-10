package ar.com.mbe.core.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Esta clase debe ser utilizada como un punto focal para todas los metodos genericos
 * para trabajo con numeros dentro de este proto-framework, para tener encapsuladas, en
 * una sola clase, todas las cuestiones asociadas a este tipo de necesidades.
 *
 * @author jmfragueiro
 * @version 20230601
 */
public abstract class N {
	public static String format(Double valor, String format) {
		NumberFormat format1 = NumberFormat.getNumberInstance(Locale.getDefault());
		DecimalFormat formater = (DecimalFormat) format1;
		formater.applyPattern(format);
		return formater.format(valor);
	}
	
	public static double round(double valor, int dec) {
		if (dec < 0) throw new IllegalArgumentException();
		
		BigDecimal bd = new BigDecimal(valor);
		bd = bd.setScale(dec, RoundingMode.HALF_UP);
		
		return bd.doubleValue();
	}
	
	public static boolean esNumerico(String valor) {
		if (valor == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(valor);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
