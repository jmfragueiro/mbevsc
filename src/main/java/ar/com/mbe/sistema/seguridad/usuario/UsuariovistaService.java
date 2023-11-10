package ar.com.mbe.sistema.seguridad.usuario;

import ar.com.mbe.base.service.Servicio;
import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.L;
import ar.com.mbe.core.common.N;
import ar.com.mbe.core.extradata.IExtraDataService;
import ar.com.mbe.core.token.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class UsuariovistaService extends Servicio<Usuariovista, Long>
        implements IUsuariovistaService, IExtraDataService {
    private final Gson gson = new Gson();
    private final ObjectMapper om = new ObjectMapper();

    @Autowired
    public UsuariovistaService(IUsuariovistaRepo repo) {
        super(repo);
        om.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Page<Usuariovista> findUsuarios(String filtro, Pageable pageable) {
        if (N.esNumerico(filtro) && !filtro.equals("")) {
            Integer documento = Integer.parseInt(filtro);
            return ((IUsuariovistaRepo) getRepo()).findUsuariosByDocumento(documento, pageable);
        } else {
            return ((IUsuariovistaRepo) getRepo()).findUsuariosByUsername(filtro.toLowerCase(), pageable);
        }
    }

    @Override
    public Usuariovista findByUsername(String username) {
        return ((IUsuariovistaRepo) getRepo()).findByUsername(username);
    }

    @Override
    public Optional<String> getUsernameByUsuarioId(Long id) {
        return ((IUsuariovistaRepo) getRepo()).getUsernameByUsuarioId(id);
    }

    @Override
    public Object create(Object origen) {
        return findByUsername(((Token) origen).getPayload().getName());
    }

    @Override
    public Object extract(Object data) {
        try {
            HashMap<String, String> mapa = (HashMap<String, String>) data;
            mapa.remove("emailcheckAt");
            mapa.remove("fechanacimiento");
            mapa.remove("fechaalta");
            mapa.remove("fechaumod");
            mapa.remove("fechabaja");
            return om.readValue(gson.toJson(mapa), Usuariovista.class);
        } catch(JsonProcessingException jpe) {
            L.info(C.MSJ_SEC_ERR_CANTEXTRACTTED);
            return null;
        }
    }

    @Override
    public Class<Usuariovista> getEntityClass() {
        return Usuariovista.class;
    }
}
