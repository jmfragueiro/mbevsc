package ar.com.mbe.core.common;

/**
 * Este tipo registro debe ser utilizado generar un esquema común de respuesta para
 * cualqueir proceso que deba devoler el estado de una operación que pueda ser de
 * error o de suceso.
 *
 * @author jmfragueiro
 * @version 20200201
 */
public record R (boolean success, String mensaje) { }
