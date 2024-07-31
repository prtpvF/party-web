package by.intexsoft.diplom.auth.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

        private final KafkaTemplate<String, KafkaMessageModel> kafkaTemplate;

        @Value("${spring.kafka.topic-auth.name}")
        private String authTopic;

        @Value("${spring.kafka.topic-verify.name}")
        private String verifyTopic;

        public void sendMessage( KafkaMessageModel kafkaMessageModel) {
            kafkaTemplate.send(authTopic, kafkaMessageModel);
        }

        public void sendVerificationCode(KafkaMessageModel messageModelDto) {
            kafkaTemplate.send(verifyTopic, messageModelDto);
        }
}
