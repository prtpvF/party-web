package by.intexsoft.diplom.chat.controller;

import by.intexsoft.diplom.chat.dto.ChatMessageDto;
import by.intexsoft.diplom.common.model.conversation.MessageModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

        @MessageMapping("/chat/{id}/sendMessage")
        @SendTo("/topic/public")
        public ChatMessageDto sendMessage(@Payload MessageModel chatMessage) {
                return chatMessage;
        }
}
