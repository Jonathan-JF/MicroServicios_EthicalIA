package Usuario.Usuario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Usuarios")
                .version("1.0")
                .description("Documentación de la API para el microservicio de usuarios")
                .contact(new Contact()
                    .name("Equipo Usuarios")
                    .email("soporte@usuarios.com")
                )
            );
    }
}
