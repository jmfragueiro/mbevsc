package ar.com.mbe.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {
    @Value("${server.entorno}")
    private String entornoEjecucion;

    @Value("${front.sistema}")
    private String sistemanombre;

    @Value("${front.codigo}")
    private String empresacodigo;

    @Value("${front.nombre}")
    private String empresanombre;

    @Value("${front.nombrecorto}")
    private String empresanombrecorto;

    @Value("${front.favicon}")
    private String empresafavicon;

    @Value("${front.canonical}")
    private String empresacanonical;

    @Value("${front.title}")
    private String empresatitle;

    @Value("${front.backendhostport}")
    private String backendhostport;

    @Value("${front.backendhostssl}")
    private String backendhostssl;

    @Value("${front.frontendhost}")
    private String frontendhost;

    @Value("${front.baseurlsufijo}")
    private String baseurlsufijo;

    /**
     * Devuelve los Parametros a utilizar en el server FrontEnd de acuerdo a los valores
     * del server donde se ejecuta el BackEnd, permitiendo distintas configuraciones en
     * funcion a cada entorno.
     *
     * @return Configuacion de Paramentros en frontConfigDTO
     */
    public ResponseEntity<RConfigData> getConfigParams() {
        return new ResponseEntity<>(
                        new RConfigData(
                                this.entornoEjecucion,
                                this.sistemanombre,
                                this.empresacodigo,
                                this.empresanombre,
                                this.empresanombrecorto,
                                this.empresafavicon,
                                this.empresacanonical,
                                this.empresatitle,
                                this.backendhostport,
                                this.backendhostssl,
                                this.frontendhost,
                                this.baseurlsufijo),
                        HttpStatus.OK);
    }
}
