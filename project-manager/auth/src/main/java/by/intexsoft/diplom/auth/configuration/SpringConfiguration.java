package by.intexsoft.diplom.auth.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

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
        private String password;

        @Value("${spring.data.redis.host}")
        private String hostname;

        @Value("${spring.data.redis.port}")
        private int port;

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

        @Bean
        public RestTemplate restTemplate() {
                return new RestTemplate();
        }

        @Bean
        public ObjectMapper objectMapper() {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                return objectMapper;
        }

        @Bean
        public JedisConnectionFactory connectionFactory() {
                RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
                configuration.setHostName(hostname);
                configuration.setPort(port);
                return new JedisConnectionFactory(configuration);
        }

        @Bean
        public RedisTemplate<String, String> template() {
                RedisTemplate<String, String> template = new RedisTemplate<>();
                template.setConnectionFactory(connectionFactory());
                template.setKeySerializer(new StringRedisSerializer());
                template.setValueSerializer(new StringRedisSerializer());
                template.setEnableTransactionSupport(true);
                template.afterPropertiesSet();
                return template;
        }
}
