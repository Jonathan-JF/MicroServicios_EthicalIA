package MicroServcios_EthicalIA.Etica;

import MicroServcios_EthicalIA.Etica.model.Etica;
import MicroServcios_EthicalIA.Etica.repository.EticaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final EticaRepository eticaRepository;

    public DataLoader(EticaRepository eticaRepository) {
        this.eticaRepository = eticaRepository;
    }

    @Override
    public void run(String... args) {
        if (eticaRepository.count() == 0) {
            Faker faker = new Faker();
            Random random = new Random();

            for (int i = 0; i < 10; i++) {
                Etica etica = new Etica();
                etica.setTexto(faker.lorem().sentence());
                etica.setLenguajeInclusivo(random.nextBoolean());
                etica.setLenguajeOfensivo(random.nextBoolean());
                etica.setObservaciones(random.nextBoolean() ? faker.lorem().sentence() : null);
                etica.setFechaAnalisis(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
                eticaRepository.save(etica);
            }
        }
    }
}
