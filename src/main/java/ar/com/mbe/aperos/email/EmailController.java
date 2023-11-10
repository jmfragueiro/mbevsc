package ar.com.mbe.aperos.email;

import ar.com.mbe.core.common.R;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/email")
public class EmailController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/send")
    @ResponseBody
    public ResponseEntity<Object> sendEmail(@RequestBody Email email) throws MessagingException {
        R resultado = emailService.sendEmail(email);

        LOGGER.info(resultado.mensaje());

        if (resultado.success()) {
            return ResponseEntity.ok().body(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }
}