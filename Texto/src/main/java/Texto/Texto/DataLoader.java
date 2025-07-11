package Texto.Texto;

import Texto.Texto.model.Texto;
import Texto.Texto.service.TextoService;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final TextoService textoService;

    public DataLoader(TextoService textoService) {
        this.textoService = textoService;
    }

    @Override
    public void run(String... args) {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            Texto texto = new Texto();
            texto.setContenido(faker.lorem().paragraph()); // contenido aleatorio
            texto.setFechaEnvio(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 30)));
            textoService.save(texto);
        }
    }
}