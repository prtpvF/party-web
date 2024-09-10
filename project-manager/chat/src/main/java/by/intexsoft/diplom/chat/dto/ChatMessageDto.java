package by.intexsoft.diplom.chat.dto;

import lombok.*;

import java.awt.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {

        private Integer conversationId;

        private String content;

        private String senderUsername;

        private Integer typeId;

        private LocalDateTime sentAt;
}
