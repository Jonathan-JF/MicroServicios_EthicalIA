package Usuario.Usuario;

import Usuario.Usuario.model.Usuario;
import Usuario.Usuario.service.UsuarioService;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioService service;

    public DataLoader(UsuarioService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            Usuario usuario = new Usuario();
            usuario.setCorreo(faker.internet().emailAddress());
            usuario.setNombre(faker.name().fullName());
            usuario.setRol(faker.options().option("ADMIN", "USER", "INVITADO"));
            usuario.setContrasena(faker.internet().password(8, 16));
            service.save(usuario);
        }
    }
}
