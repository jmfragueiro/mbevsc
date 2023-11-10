package ar.com.mbe.base.repos;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.T;

/**
 * Esta clase de excepcion deberia utilizarse para representar todos el error específico de no encontrar
 * una instancia determinada en un repositorio del sistema.
 *
 * @author jmfragueiro
 * @version 20230601
 */
public class ItemNotFoundException extends RepoException {
    public ItemNotFoundException(String entidad, String item) {
        super(C.MSJ_ERR_DB_NOITEM
                .concat(C.SYS_CAD_SPACE)
                .concat(entidad)
                .concat(C.SYS_CAD_OPENTYPE)
                .concat(item)
                .concat(C.SYS_CAD_CLOSETPE),
                T.getNombreMetodoLlamante(2));
    }
}
