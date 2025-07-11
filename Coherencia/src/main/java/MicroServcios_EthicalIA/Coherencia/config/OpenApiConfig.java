package MicroServcios_EthicalIA.Coherencia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Microservicio de Coherencia - EthicalIA")
                .version("1.0.0")
                .description("API para la gestión de Coherencias generados por el microservicio EthicalIA.")
                .contact(new Contact()
                    .name("Equipo EthicalIA")
                    .email("contacto@ethicalia.com")
                )
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")
                )
            );
    }
}
