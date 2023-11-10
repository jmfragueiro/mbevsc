package ar.com.mbe.core.config;

import ar.com.mbe.core.common.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = C.SYS_CAD_CONFIGURL)
public class ConfigController {
    private final ConfigService configService;

    @Autowired
    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/params")
    public ResponseEntity<RConfigData> getConfigParams() {
        return configService.getConfigParams();
    }
}
