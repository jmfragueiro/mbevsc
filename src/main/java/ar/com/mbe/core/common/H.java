package ar.com.mbe.core.common;

import ar.com.mbe.core.auth.EHttpReqAuthType;

/**
 * Este tipo registro debe ser utilizado generar un formato común de respuesta HTTP
 * ante errores o mensajes que no sean propios de una Entidad específica solicitada.
 *
 * @author jmfragueiro
 * @version 20200201
 */
public record H(String timestamp,
				Integer status,
				String error,
				String object,
				String message,
				String path,
				EHttpReqAuthType type) { }
