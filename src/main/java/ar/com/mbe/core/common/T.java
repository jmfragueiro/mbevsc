package ar.com.mbe.core.common;

import com.google.gson.Gson;
import jakarta.xml.bind.DatatypeConverter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.JDBCException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Esta clase debe ser utilizada como un punto focal para todos los metodos
 * genericos utilitarios dentro de este proto-framework, de manera de tener
 * encapsuladas, en una sola clase, todas las operaciones de este tipo.
 * tipo de necesidades.
 *
 * @author jmfragueiro
 * @version 20230601
 */
public abstract class T {
    /**
     * Este metodo retorna el lugar exacto desde donde es llamado como una cadena presentada como:
     * [CLASE:METODO:NroLinea]. Hay que tener en cuenta que el número de linea puede estar asociado
     * (en una llamada desde una excepcion) a la línea desde donde se gestiona la exepcion (catch).
     *
     * @param index, el indice de metodo llamante requerido.
     * @return retorna el lugar exacto desde donde es llamado.
     */
    public static String getNombreMetodoLlamante(int index) {
        StackTraceElement ste = new Exception().getStackTrace()[index];
        return C.SYS_CAD_OPENTYPE.concat(ste.getClassName())
                                 .concat(C.SYS_CAD_LOGSEP)
                                 .concat(ste.getMethodName())
                                 .concat(C.SYS_CAD_LOGSEP)
                                 .concat(String.valueOf(ste.getLineNumber()))
                                 .concat(C.SYS_CAD_CLOSETPE);
    }

    /**
     * Este metodo permite generar un mensaje tipo excepción con el template:
     * "CADENA DE MENSAJE [DATOS ADJUNTOS]"
     * para que pueda ser registrado en alguno de las interfaces de salida del sistema.
     *
     * @param mensaje la cadena del mensaje a formatear
     * @param adjunto una cadena de datos adjuntos para agregar al mensaje a formatear
     * @return el mensaje con el formato "de excepción" comentado
     */
    public static String getExcFormatedMesg(String mensaje, String adjunto) {
        return mensaje.concat(C.SYS_CAD_SPACE)
                      .concat(C.SYS_CAD_OPENTYPE)
                      .concat(T.getEmptyStringOnNull(adjunto))
                      .concat(C.SYS_CAD_CLOSETPE);
    }

    /**
     * Este metodo permite transformar un objeto cualquiera a una cadena con formato JSON.
     *
     * @param objeto el objeto a serializar al formato JSON
     * @return la cadena con formato JSON respectiva al objeto pasado como argumento
     */
    public static String getObjetoSerializadoAStringJSON(Object objeto) {
        return new Gson().toJson(((objeto != null) ? objeto : C.SYS_CAD_NULL));
    }

    /**
     * Este metodo permite obtener un nombre de clase formateado para ser mostrado en el sistema, y
     * con la posibilidad de que el mismo sea filtrado removiendo cierta parte del nombre de clase
     * para que quede solamente un componente del nombre.
     *
     * @return el nombre obtenido desde el formateo
     */
    public static String getNombreClaseFormateado(Class clase, String remover) {
        return clase.getSimpleName().replace(remover, "");
    }

    /**
     * Este metodo permite obtener un mensaje de excepcion mas claro cuando se trata de una
     * excepcion originada en un problema de SQL. En caso de no ser una excepción originada
     * en un problema de SQL, devuelve solamente el mensaje de la excepcion.
     *
     * @param ex la excepcion lanzada
     * @return un mensaje que puede tener datos extras si es una excepción originada en un problema SQL
     */
    public static String getMesgSQLException(Exception ex) {
        return (ex.getCause() != null && ex.getCause() instanceof JDBCException)
                ? T.getExcFormatedMesg(ex.getMessage(), ((JDBCException) ex.getCause()).getSQLException().getMessage())
                : ex.getMessage();
    }
	
	/**
     * Este metodo retorna un hash con md5 de un string recibido.
	 *
     * @param texto el texto para el cual calcular el hash
     * @return el hash obtenido
     */
    public static String getMd5Token(String texto) {
        final StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            md.update(texto.getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter.printHexBinary(digest);
            sb.append(myHash);
        } catch (Exception exc) {
            sb.append("ImposibleObtener");
        }

        return sb.toString();
    }

    /**
     * Este metodo retorna un hash con md5 de un string recibido.
     *
     * @param texto el texto para el cual calcular el hash
     * @return el hash obtenido
     */
    public static String getSha256Token(String texto) {
        String sb;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            sb = DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (Exception exc) {
            sb = C.SYS_CAD_NULL;
        }

        return sb;
    }

    /**
     * Este metodo permite limpiar un string recibido, quitando caracteres no deseados
     *
     * @param texto
     * @return texto limpio de caracteres no deseados
     */
    public static String getlimpiarString(String texto) {
        AtomicReference<String> txt = new AtomicReference<>(texto);
        char[] array = {
                '[',
                (char) 126, // Chr 126 = ~
                (char) 124, // Chr 124 = |
                (char) 44,  // Chr  44 = ,
                (char) 59,  // Chr  59 = ;
                (char) 34,  // Chr  34 = Comillas
                (char) 39,  // Chr  39 = Apostrofe
                (char) 96,  // Chr  96 = Apostrofe
                (char) 239, // Chr 239 = Apostrofe
                ']'
        };

        return texto.replaceAll(Arrays.toString(array), String.valueOf((char)0));
    }

    /**
     * Este metodo permite obtener un String vacio ('') si el String
     * pasado como parametro es nulo, si no, devuelve el String parametro.
     *
     * @param objeto
     * @return un objeto para controlar si es nulo
     */
    public static String getEmptyStringOnNull(String objeto) {
        return ((objeto == null) ? C.SYS_CAD_NULL : objeto);
    }

    /**
     * Este metodo permite obtener un String con un error mas o menos formateado.
     *
     * @param error el error principal
     * @param extra una cadena extra para aclarar el error
     * @param username un nombre de usuario por si hiciera falta
     * @return la cadena de error formateada
     */
    public static String getCadenaErrorFormateada(String error, String extra, String username) {
        return (Strings.isNotEmpty(error) ? error.concat(C.SYS_CAD_LOGSEP) : C.SYS_CAD_ERROR)
                .concat(C.SYS_CAD_SPACE)
                .concat(Strings.isNotEmpty(extra) ? extra : C.SYS_CAD_NULL)
                .concat(C.SYS_CAD_SPACE)
                .concat(C.SYS_CAD_OPENTYPE)
                .concat(Strings.isNotEmpty(username) ? username : C.SYS_CAD_NULL)
                .concat(C.SYS_CAD_CLOSETPE);
    }

    /**
     * Este metodo permite verificar un CUIT para asegurar que es valido.
     *
     * @param cuit el CUIT a validarse
     * @return retorna true si es valido, de lo contrario retorna false
     */
    public static Boolean esCUITValido(String cuit) {
        //Elimino todos los caracteres que no son números
        cuit = cuit.replaceAll("[^\\d]", "");

        //Controlo si son 11 números los que quedaron, si no es el caso, ya devuelve falso
        if (cuit.length() != 11) {
            return false;
        }

        //Convierto la cadena que quedó en una matriz de caracteres
        String[] cuitArray = cuit.split("");

        //Inicializo una matriz por la cual se multiplicarán cada uno de los dígitos
        Integer[] serie = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

        //Creo una variable auxiliar donde guardo los resultados del calculo del número validador
        Integer aux = 0;

        //Recorro las matrices de forma simultanea, sumando los productos de la serie por el número en la misma posición
        for (int i = 0; i < 10; i++) {
            aux += Integer.parseInt(cuitArray[i]) * serie[i];
        }

        //Hago como se especifica: 11 menos el resto de la división de la suma de productos anterior por 11
        aux = 11 - (aux % 11);

        //Si el resultado anterior es 11 el código es 0
        if (aux == 11) {
            aux = 0;
            //o si el resultado anterior es 10 el código es 9
        } else if (aux == 10) {
            aux = 9;
        }

        //Devuelve verdadero si son iguales, falso si no lo son
        //(Esta forma esta dada para prevenir errores, se puede usar:
        // return Integer.valueOf(cuitArray[11]) == aux;)
        return Objects.equals(Integer.valueOf(cuitArray[10]), aux);
    }

    /**
     * Este metodo permite obtener una cadena aleatoria.
     *
     * @param longitud la longitud para la cadena deseada
     * @return retorna la cadena creada aleatoriamente
     */
    public static String cadenaAleatoria(int longitud) {
        // El banco de caracteres
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        // La cadena en donde iremos agregando un carácter aleatorio
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
            char caracterAleatorio = banco.charAt(indiceAleatorio);
            cadena += caracterAleatorio;
        }
        return cadena;
    }

    /**
     * Este metodo permite obtener un valor entero aleatorio dentro del rango deseado.
     *
     * @param minimo el piso del rango deseado
     * @param maximo el techo para el rango deseado
     * @return retorna el entero creado aleatoriamente dentro del rango requerido
     */
    public static int numeroAleatorioEnRango(int minimo, int maximo) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }
}
