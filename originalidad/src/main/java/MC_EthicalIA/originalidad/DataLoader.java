package MC_EthicalIA.originalidad;

import MC_EthicalIA.originalidad.model.Originalidad;
import MC_EthicalIA.originalidad.service.OriginalidadService;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final OriginalidadService service;

    public DataLoader(OriginalidadService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            Originalidad originalidad = new Originalidad();
            originalidad.setTexto(faker.lorem().characters(13, 13, true));
            originalidad.setPorcentajePlagio(faker.number().randomDouble(2, 0, 100));
            originalidad.setFuentesDetectadas(faker.internet().url());
            originalidad.setFechaAnalisis(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 30)));
            service.save(originalidad);
        }
    }
}
