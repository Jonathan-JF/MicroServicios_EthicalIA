package Historial.Historial;

import Historial.Historial.model.Historial;
import Historial.Historial.repository.HistorialRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataLoader {

    private final HistorialRepository historialRepository;

    public DataLoader(HistorialRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    @PostConstruct
    public void loadData() {
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            Historial historial = new Historial();
            historial.setTextoId((long) faker.number().numberBetween(1, 100));
            historial.setFechaAnalisis(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 365)));
            historial.setDescripcion(faker.lorem().sentence());
            historial.setConsentimiento(random.nextBoolean());

            historialRepository.save(historial);
        }
    }
}
