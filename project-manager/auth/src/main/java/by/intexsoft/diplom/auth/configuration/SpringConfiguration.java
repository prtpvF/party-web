package by.intexsoft.diplom.auth.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

        @Value("${keycloak.server-url}")
        private String serverUrl;

        @Value("${keycloak.realm}")
        private String realm;

        @Value("${keycloak.client-id}")
        private String clientId;

        @Value("${keycloak.username}")
        private String username;

        @Value("${keycloak.password}")
        private String password;;

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

        @Bean
        Keycloak keycloak() {
            return KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(username)
                    .password(password)
                    .build();
        }
}
