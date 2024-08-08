package by.intexsoft.diplom.auth.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

        @Bean
        public NewTopic newAuthTopic() {
            return new NewTopic("auth-topic",
                    1,
                    (short) 1);
        }

        @Bean
        public NewTopic newVerifyTopic() {
            return new NewTopic("verify-topic",
                    1,
                    (short) 1);
        }

        @Bean
        public ProducerFactory<String, KafkaMessageModel> producerFactory() {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);  // Disable type information in headers if not needed
            return new DefaultKafkaProducerFactory<>(props);
        }

        @Bean
        public KafkaTemplate<String, KafkaMessageModel> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }
}