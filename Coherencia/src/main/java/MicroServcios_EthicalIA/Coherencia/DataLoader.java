package MicroServcios_EthicalIA.Coherencia;

import com.ethicaia.model.Coherencia;
import com.ethicaia.repository.CoherenciaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    private final CoherenciaRepository coherenciaRepository;

    public CoherenciaDataLoader(CoherenciaRepository coherenciaRepository) {
        this.coherenciaRepository = coherenciaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            Coherencia coherencia = new Coherencia();
            coherencia.setTexto(faker.lorem().paragraph());
            coherencia.setPuntuacionCoherencia(faker.number().randomDouble(2, 40, 100));
            coherencia.setSugerencias(random.nextBoolean() ? faker.lorem().sentence() : null);
            coherencia.setFechaAnalisis(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
            coherenciaRepository.save(coherencia);
        }
    }
}