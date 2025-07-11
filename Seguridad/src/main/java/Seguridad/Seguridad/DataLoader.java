package Seguridad.Seguridad;

import Seguridad.Seguridad.model.Seguridad;
import Seguridad.Seguridad.service.SeguridadService;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final SeguridadService service;

    public DataLoader(SeguridadService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            Seguridad s = new Seguridad();
            s.setContenidoEncriptado(faker.lorem().characters(20));
            s.setFechaTransito(LocalDateTime.now().minusMinutes(faker.number().numberBetween(1, 120)));
            service.save(s);
        }
    }
}