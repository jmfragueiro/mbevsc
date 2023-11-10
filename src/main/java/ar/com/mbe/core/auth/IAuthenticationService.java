package ar.com.mbe.core.auth;

import org.springframework.security.core.Authentication;

/**
 * Esta interface establece el comportamiento necesario para "enganchar" este "proto-framework"
 * con el mecanismo de Auntenticación de springboot, es decir para dar soporte efectivo a las
 * operaciones de autenticación. La implementación efecgiva dentro de este "proto-framework"
 * utilizará la funcionalidad dada por nuestros "ITOKEN" uniendo ambos mundos y permitiendo
 * flexibilidad al soportar la inyección "parametrizada" del servicio de Payload de los Itokens
 * a utilizarse (es decir permitiendo definir el servicio concreto via application.properties
 * en la propiedad: security.payload.service).
 *
 * @author jmfragueiro
 * @version 20230601
 */
public interface IAuthenticationService {
    Authentication construct(String seed);

    Authentication construct(String username, String password);

    Authentication authenticate(Authentication auth);

    void login(Authentication auth);

    void logout(Authentication auth);
}
