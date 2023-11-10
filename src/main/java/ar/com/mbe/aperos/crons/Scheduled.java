package ar.com.mbe.aperos.crons;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Scheduled {
    private final long SEGUNDO = 1000;
    private final long MINUTO = SEGUNDO * 60;
    private final long HORA = MINUTO * 60;
    private final long MINUTO_5 = MINUTO * 5;
    private final long MINUTO_15 = MINUTO * 15;
    private final long MINUTO_30 = MINUTO * 30;

    //    @Scheduled(cron = "0 0 12")

    /**
     * Esta Funcion Verifica los Envio de las Entradas que fueron adquiridas mediante el modulo de Eventos
     * En Particular este proceso es para el Evento de la Fiesta del Litoral 2022
     * Verifica Entradas No Enviadas al Comprador y que tiene Estado PAGADO
     */
    @Async
    @org.springframework.scheduling.annotation.Scheduled(fixedDelay = MINUTO_15)
    //@org.springframework.scheduling.annotation.Scheduled(initialDelay = MINUTO, fixedDelay = MINUTO_15)
    public void enviarEntradasPagadasPorEvento() {
        //procesoenvioService.enviarEntradasPagadasPorEvento("LITORAL2022");
    }
}