package by.intexsoft.diplom.publicapi.configuration;


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
                description = "open API doc for public service",
                title = "Open API specification - Mihail",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local Enviroment",
                        url = "http://localhost:8092"
                )
        }
)
@SecurityScheme(
        name = "authorization",
        description = "JWT auth",
        scheme = "bearer",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "authorization"
)
@Configuration
public class SwaggerConfig {

        @Bean
        public GroupedOpenApi authModuleApi() {
            return GroupedOpenApi.builder()
                    .group("public-module-api")
                    .pathsToMatch("/public/**")
                    .build();
        }
}