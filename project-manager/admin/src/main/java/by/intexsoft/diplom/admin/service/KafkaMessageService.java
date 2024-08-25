package by.intexsoft.diplom.admin.service;

import by.intexsoft.diplom.admin.kafka.KafkaMessageModel;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageService {

        private final KafkaTemplate<String, KafkaMessageModel> kafkaTemplate;

        @Value("${spring.kafka.topic-organizer.name}")
        private String organizerTopic;

        @Value("${request-apply}")
        private String applyRequestText;

        @Value("${request-decline}")
        private String declineRequestText;

        public void sendMessageToOrganizerTopic(PartyEntity party, Boolean flag) {
            KafkaMessageModel kafkaMessage = new KafkaMessageModel();
            PersonModel organizer = party.getOrganizer();
            kafkaMessage.setTopic(organizerTopic);
            kafkaMessage.setUsername(organizer.getUsername());
            kafkaMessage.setToEmail(organizer.getEmail());

            if (flag) {
                kafkaMessage.setData(String.format(applyRequestText,
                        organizer.getUsername(),
                        party.getStatus().getStatus(),
                        party.getName()));
            } else {
                kafkaMessage.setData(String.format(declineRequestText,
                        organizer.getUsername(),
                        party.getStatus().getStatus(),
                        party.getName()));
            }
            kafkaTemplate.send(organizerTopic, kafkaMessage);
        }
}