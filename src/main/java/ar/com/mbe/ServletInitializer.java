package ar.com.mbe;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    // inicializacion de sprinboot
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MBEApplication.class);
    }
}
