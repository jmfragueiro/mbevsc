package ar.com.mbe.core.jws;

import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.F;
import ar.com.mbe.core.exception.AuthException;
import ar.com.mbe.refimpl.DefaultExtraDataService;
import ar.com.mbe.core.extradata.IExtraDataService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class JwsService implements IJwsService {
    private final IExtraDataService edService;

    @Value("${security.signing-key}")
    private String signingKey;

    @Value("${security.realm}")
    private String realm;

    @Autowired
    public JwsService(List<IExtraDataService> edsList, @Value("${extradata.service}") String edsname) {
        edService = (edsList != null) ? edsList.stream()
                                               .filter(e -> e.getClass().getName().equals(edsname))
                                               .findFirst()
                                               .orElse(new DefaultExtraDataService()) : null;
    }

    private Claims getBody(String jws) {
        return Jwts.parserBuilder()
                   .setSigningKey(Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8)))
                   .build()
                   .parseClaimsJws(jws)
                   .getBody();
    }

    public String generateJws(IJwsDataHelper source) {
        var jws = Jwts.builder()
                      .setId(this.realm)
                      .setSubject(source.getId().toString())
                      .setIssuedAt(F.toDate(source.getIssuedAt()))
                      .claim("authorities", source.authClaim());

        if (edService != null) {
            jws.claim("ted", edService.create(source));
        }

        return jws.signWith(Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512).compact();
    }

    public String validateJws(String jws) {
        try {
            if (jws == null) {
                throw new AuthException(C.MSJ_SEC_ERR_BADTOKEN, C.MSJ_SEC_ERR_EMPTYCLAIM);
            }

            if (!getBody(jws).getId().equals(this.realm)) {
                throw new AuthException(C.MSJ_SEC_ERR_BADJWTSIGN, C.MSJ_SEC_ERR_BADJWT);
            }
        } catch (ExpiredJwtException e) {
            // ACA NO PASA NADA, SE RESUELVE MAS ADELANTE...
        } catch (SignatureException e) {
            throw new AuthException(C.MSJ_SEC_ERR_BADJWTSIGN, e.getMessage());
        } catch (MalformedJwtException e) {
            throw new AuthException(C.MSJ_SEC_ERR_BADTOKEN, e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new AuthException(C.MSJ_SEC_ERR_TOKENNOTSUP, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new AuthException(C.MSJ_SEC_ERR_EMPTYCLAIM, e.getMessage());
        } catch (Exception e) {
            throw new AuthException(C.MSJ_SEC_ERR_TOKENREINIT, e.getMessage());
        }

        return jws;
    }

    public String getIdFromJws(String jws) {
        return getBody(jws).getSubject();
    }

    public Object getExtraDataFromJws(String jws) {
        return (edService != null ? edService.extract(getBody(jws).get("ted")) : null);
    }
}
