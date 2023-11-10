package ar.com.mbe.core.config;

/**
 * Esta Clase DTO, es utilizada para resolver la devolucion de los parametros para
 * hacer funcinar el FrontEnd de acuerdo a la determinacion que se realiza segun el
 * servidor donde se esta ejecutando el BackEnd.
 *
 * @author lschwegler
 * @version 20220501
 */
public record RConfigData(String entornoejecucion,
                          String sistemanombre,
                          String empresacodigo,
                          String empresanombre,
                          String empresanombrecorto,
                          String empresafavicon,
                          String empresacanonical,
                          String empresatitle,
                          String backendhostport,
                          String backendhostssl,
                          String frontendhost,
                          String baseurlsufijo) { }
