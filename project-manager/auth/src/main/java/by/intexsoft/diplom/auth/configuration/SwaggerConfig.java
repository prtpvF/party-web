package by.intexsoft.diplom.auth.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Mihail",
                        email = "caplyginmihail48@gmail.com",
                        url = "th: @who_e_to"
                ),
                description = "open API doc for auth service",
                title = "Open API specification - Mihail",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local Enviroment",
                        url = "http://localhost:8090"
                )
        }
)
@SecurityScheme(
        name = "token",
        description = "JWT auth",
        scheme = "bearer",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "token"
)
@Configuration
public class SwaggerConfig {

        @Bean
        public GroupedOpenApi authModuleApi() {
            return GroupedOpenApi.builder()
                    .group("auth-module-api")
                    .pathsToMatch("/auth/**") // Укажите пути, которые будут включены в документацию
                    .build();
        }
}