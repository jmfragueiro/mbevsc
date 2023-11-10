package ar.com.mbe.core.common;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Esta clase debe ser utilizada como un punto focal para todas los metodos genericos
 * para trabajo con fechas dentro de este proto-framework, para tener encapsuladas, en
 * una sola clase, todas las cuestiones asociadas a este tipo de necesidades.
 *
 * @author jmfragueiro
 * @version 20200201
 */
public abstract class F {
    public static Date toDate(LocalDate localdate) {
        return ((localdate != null) ? java.sql.Date.valueOf(localdate) : null);
    }

    public static Date toDate(LocalDateTime localdate) {
        return ((localdate != null) ? java.sql.Date.valueOf(localdate.toLocalDate()) : null);
    }

    public static LocalDate toLocalDate(Date date) {
        return ((date != null) ? LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)) : null);
    }

    public static Date truncateDate(Date date) {
        return toDate(toLocalDate(date));
    }

    public static Date inicioMes(Date fecha) {
        fecha = truncateDate(fecha);
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static Date finMes(Date fecha) {
        fecha = truncateDate(fecha);
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static String getNowDateAsString(String formato) {
        String mifecha = "";

        if (formato.equals("")) {
            mifecha = (String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + String.valueOf(
                    Calendar.getInstance().get(Calendar.MONTH) + 1) + String.valueOf(
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + String.valueOf(
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + String.valueOf(
                    Calendar.getInstance().get(Calendar.MINUTE)) + String.valueOf(
                    Calendar.getInstance().get(Calendar.SECOND)));
        } else if (formato.equals("d/m/y")) {
            mifecha = (String.format("%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                       + "/"
                       + String.format("%02d", Calendar.getInstance()
                                                       .get(Calendar.MONTH)
                                               + 1)
                       + "/"
                       + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        }

        return mifecha;
    }

    public static Date fromString(String source, String pattern) {
        return toDate(LocalDate.parse(source, DateTimeFormatter.ofPattern(pattern)));
    }

    public static String format(Date fecha, String formato) {
        return new SimpleDateFormat(formato).format(fecha);
    }

    public static Integer getNumeroDiaSemana(LocalDate fecha) {
        return fecha.getDayOfWeek().getValue();
    }

    public static Integer getNumeroDiaSemanaPostgreSQL(LocalDate fecha) {
        return (fecha.getDayOfWeek().equals(DayOfWeek.SUNDAY)) ? 0 : fecha.getDayOfWeek().getValue();
    }

    public static List<Date> getListaEntreFechas(Date fechaInicio, Date fechaFin) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(fechaInicio);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(fechaFin);
        List<Date> listaFechas = new ArrayList<>();
        while (!c1.after(c2)) {
            listaFechas.add(c1.getTime());
            c1.add(Calendar.DAY_OF_MONTH, 1);
        }
        return listaFechas;
    }

    public static LocalDateTime ahoraLocalDateTime() {
        return LocalDateTime.now();
    }

    public static Date ahoraDate() {
        return Date.from(Instant.now());
    }

    public static Boolean esFechaPasada(LocalDateTime fecha) {
        return fecha.isBefore(LocalDateTime.now());
    }

    public static String trasnformarLDTaString(LocalDateTime ldt) {
        return ldt.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static LocalDateTime fechaActualLocalDateTime() {

        return LocalDateTime.now();
    }

    public static Date fechaActualDate() {
        return Date.from(Instant.now());
    }

    public static LocalDate fechaActualLocalDate() {
        return LocalDate.now();
    }

    public static String horaActual() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static String horaActualCompleto() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static Integer parteFechaActual(String parte) {
        int valor = 0;

        if (parte.equals("D")) {
            valor = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
        if (parte.equals("M")) {
            valor = Calendar.getInstance().get(Calendar.MONTH);
        }
        if (parte.equals("Y")) {
            valor = Calendar.getInstance().get(Calendar.YEAR);
        }

        return valor;
    }

    public static Integer parteFechaTipoLocalDate(LocalDate fecha, String parte) {
        int valor = 0;

        if (parte.equals("D")) {
            valor = fecha.getDayOfMonth();
        }
        if (parte.equals("M")) {
            valor = fecha.getMonthValue();
        }
        if (parte.equals("Y")) {
            valor = fecha.getYear();
        }

        return valor;
    }
}
