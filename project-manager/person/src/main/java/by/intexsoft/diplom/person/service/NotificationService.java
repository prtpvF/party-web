package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.person.kafka.KafkaMessageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for notification what will be sent via Kafka.
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

        private final KafkaTemplate<String, KafkaMessageModel> kafkaTemplate;

        @Value("${spring.kafka.topic-participation.name}")
        private String participationTopic;

        @Value("${participation-request-decline}")
        private String decline;

        @Value("${participation-request-apply}")
        private String apply;

        public void sendNotificationAboutParticipationRequest(ParticipationRequestModel request,
                                                               boolean flag) {
            PersonModel personFromRequest = request.getPerson();
            KafkaMessageModel kafkaMessage = new KafkaMessageModel();
            if(flag) {
                kafkaMessage.setData(String.format(apply,
                        personFromRequest.getUsername(),
                        request.getParty().getName()));
            }
            else {
                kafkaMessage.setData(String.format(decline,
                        personFromRequest.getUsername(),
                        request.getParty().getName()));
            }
            kafkaMessage.setTopic(participationTopic);
            kafkaMessage.setToEmail(personFromRequest.getEmail());
            kafkaMessage.setUsername(personFromRequest.getUsername());
            kafkaTemplate.send(participationTopic, kafkaMessage);
        }
}
