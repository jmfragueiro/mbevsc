package ar.com.mbe.refimpl;

import ar.com.mbe.core.token.ITokenPayloadService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultTokenPayloadService implements ITokenPayloadService<RDefaultUser> {
    @Override
    public Optional<RDefaultUser> findByName(String name) {
        ///////////////////////////////////////////////////////
        // ESTO ES PARA GENERAR USUARIOS Y CLAVES Y PROBAR:  //
        // (hay que debuggear y parar en la captura de pass  //
        //  y guardar el pass generado en la base de datos)  //
        ///////////////////////////////////////////////////////
        // @Autowired                                        //
        // private BCryptPasswordEncoder passEncoder;        //
        //                                                   //
        // Usuario us = new Usuario();                       //
        // us.setUsername("jmfragueiro");                    //
        // us.setPassword(passEncoder.encode("fito"));       //
        // String pass = us.getPassword();                   //
        ///////////////////////////////////////////////////////
        return Optional.of(
                new RDefaultUser(1L, name,
                                 "$2a$10$O5s2/MhWhF.pLRdnHzSGMO5g.6jZCngNGoh0RsPIY1.CNPdQPuzvG",
                                 false,
                                 true,
                                 false));
    }
}
