package by.intexsoft.diplom.notification.kafka;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessageModel {

        private String topic;
        private String toEmail;
        private String verificationCode;
        private String username;
}
