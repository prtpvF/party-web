package by.intexsoft.diplom.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

        private Integer conversationId;

        private String content;

        private String senderUsername;

        private Integer typeId;

        private LocalDateTime sentAt;
}
