package by.intexsoft.diplom.auth.kafka;

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
