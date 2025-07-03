package ReporteGlobal.ReporteGlobal;

import ReporteGlobal.ReporteGlobal.model.ReporteGlobal;
import ReporteGlobal.ReporteGlobal.service.ReporteGlobalService;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final ReporteGlobalService service;

    public DataLoader(ReporteGlobalService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            ReporteGlobal reporte = new ReporteGlobal();
            reporte.setCantidadAnalisis(faker.number().numberBetween(10, 1000));
            reporte.setErroresSistema(faker.number().numberBetween(0, 20));
            reporte.setFechaReporte(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 60)));
            reporte.setObservaciones(faker.lorem().sentence(8, 3));
            service.save(reporte);
        }
    }
}
