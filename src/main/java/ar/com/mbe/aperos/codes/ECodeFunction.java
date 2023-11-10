package ar.com.mbe.aperos.codes;

/**
 * Esta enumeracion se utiliza para definir las funciones para las cuales
 * se pueden generar codigos (barra, QR, etc.) especioficos, de manera de
 * poder identificar como generar el codigo concreto y, despues al volver,
 * como interpretar la cadena leida y como obtener los datos asociados.
 * NOTA: seguir agregando en función a los distintos tipos de codigos a ser
 * utilizados.
 *
 * @author lschwegler
 * @version 20220501
 */
public enum ECodeFunction {
    GEN_CAPTURA_FOTO("9999"),
    GEN_RESET_PASS("9998"),
    GEN_CHECK_EMAIL("9997"),
    PA_BIEN("pabi"),
    TR_TRAMITE("trtr"),
    TR_PASELOTE("trpl"),
    EV_VENTA("evve"),
    EV_ENTRADA("even");

    private final String prefijo;

    ECodeFunction(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getPrefijo() {
        return this.prefijo;
    }
}
