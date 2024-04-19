package ar.com.mbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MBEApplication {
    // punto de entrada a la aplicación
    public static void main(String[] args) {
        SpringApplication.run(MBEApplication.class, args);
    }
}
