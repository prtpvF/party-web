package by.intexsoft.diplom.person.kafka;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessageModel {

        private String topic;
        private String toEmail;
        private String data;
        private String username;

}
