package by.intexsoft.diplom.person.kafka;

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

        @Value("${spring.kafka.topic-participation.name}")
        private String participationTopic;

        public void sendMessage( KafkaMessageModel kafkaMessageModel) {
            kafkaTemplate.send(participationTopic, kafkaMessageModel);
        }

}
