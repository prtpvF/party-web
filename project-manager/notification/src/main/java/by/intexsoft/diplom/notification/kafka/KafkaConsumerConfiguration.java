package by.intexsoft.diplom.notification.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

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
        public ConsumerFactory<String, KafkaMessageModel> consumerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group-id");
            configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
            configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
            return new DefaultKafkaConsumerFactory<>(configProps,
                    new StringDeserializer(),
                    new JsonDeserializer<>(KafkaMessageModel.class));
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, KafkaMessageModel> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, KafkaMessageModel> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }
}