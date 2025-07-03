package MC_EthicalIA.informe;

import MC_EthicalIA.informe.model.informe;
import MC_EthicalIA.informe.service.informeService;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final informeService service;

    public DataLoader(informeService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            informe inf = new informe();
            inf.setTextoId(faker.number().randomNumber());
            inf.setResumen(faker.lorem().sentence(8, 3));
            inf.setUrlPdfGenerado(faker.internet().url());
            inf.setFechaGeneracion(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 30)));
            service.save(inf);
        }
    }
}
