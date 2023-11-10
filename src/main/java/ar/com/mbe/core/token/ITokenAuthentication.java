package ar.com.mbe.core.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

/**
 * Esta interface de marcación (inicialmente) representa la unión entre el mundo de los ITOKEN (del
 * presente "proto-framework") y del mecanismo de seguridad de springboot basado en el concepto de
 * Autenticación (Authentication) y Credencial (CredentialsContainer).
 *
 * @author jmfragueiro
 * @version 20230601
 * @param <K> la clase de un identificador posible para el tipo de ITOKEN
 * @param <U> la clase del objeto (payload) a ser mantenido por el ITOKEN
 * @param <T> la clase que implementa el ITOKEN
 */
public interface ITokenAuthentication<K, U extends ITokenPayload, T extends IToken<K, U>>
        extends Authentication, CredentialsContainer {
}
