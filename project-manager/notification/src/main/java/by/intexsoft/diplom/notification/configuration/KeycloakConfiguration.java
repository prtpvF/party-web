package by.intexsoft.diplom.notification.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration {

        @Value("${keycloak.server-url}")
        private String SERVER_URL;
        @Value("${keycloak.realm}")
        private String REALM;
        @Value("${keycloak.client-id}")
        private String CLIENT_ID;
        @Value("${keycloak.grant-type}")
        private String GRANT_TYPE;
        @Value("${keycloak.username}")
        private String USERNAME;
        @Value("${keycloak.password}")
        private String PASSWORD;


        @Bean
        public Keycloak keycloak() {
            return KeycloakBuilder.builder()
                    .serverUrl(SERVER_URL)
                    .realm(REALM)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(CLIENT_ID)
                    .grantType(GRANT_TYPE)
                    .username(USERNAME)
                    .password(PASSWORD)
                    .build();
        }
}
