package ar.com.mbe.aperos.codes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
public class CodeController {
    @Autowired
    private CodeService codeService;

    @GetMapping(path = "/token/{key}", produces = "application/json")
    public Object datosByToken(@PathVariable("key") String token) {
        return codeService.getDatosByToken(token);
    }
}
