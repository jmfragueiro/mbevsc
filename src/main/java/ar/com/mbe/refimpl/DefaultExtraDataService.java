package ar.com.mbe.refimpl;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.L;
import ar.com.mbe.core.extradata.IExtraDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class DefaultExtraDataService implements IExtraDataService {
    private final Gson gson = new Gson();
    private final ObjectMapper om = new ObjectMapper();

    public DefaultExtraDataService() {
        //om.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        //om.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        //om.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Object create(Object origen) {
        return origen.getClass().toString();
    }

    @Override
    public Object extract(Object ted) {
        try {
            return om.readValue(gson.toJson(ted.toString()), String.class);
        } catch(JsonProcessingException jpe) {
            L.info(C.MSJ_SEC_ERR_CANTEXTRACTTED);
            return null;
        }
    }
}
