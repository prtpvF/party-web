package by.intexsoft.diplom.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

        private static final String HOSTNAME = "localhost";
        private static final int REDIS_PORT = 6379;

        @Bean
        public JedisConnectionFactory connectionFactory() {
            RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
            configuration.setHostName(HOSTNAME);
            configuration.setPort(REDIS_PORT);
            return new JedisConnectionFactory(configuration);
        }

        @Bean
        public RedisTemplate<String, Object> template() {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory());
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(new StringRedisSerializer());
            template.setEnableTransactionSupport(true);
            template.afterPropertiesSet();
            return template;
        }
}