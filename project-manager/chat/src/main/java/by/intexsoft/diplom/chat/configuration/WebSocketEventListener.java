package by.intexsoft.diplom.chat.configuration;

import by.intexsoft.diplom.chat.dto.ChatMessageDto;
import by.intexsoft.diplom.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

        private final Integer LEAVE_CHAT_TYPE_ID = 1;
        private final SimpMessageSendingOperations messagingTemplate;

        @EventListener
        public void handleWebSocketDisconnectListener(
                SessionDisconnectEvent event
        ) {
                StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
                String username = (String) headerAccessor.getSessionAttributes().get("username");

                if(username != null) {
                        log.info("user is disconnected: {}", username);
                        var chatMessage = ChatMessageDto
                                .builder()
                                .typeId(LEAVE_CHAT_TYPE_ID)
                                .senderUsername(username)
                                .build();
                        messagingTemplate.convertAndSend("/topic/public", chatMessage);
                }
        }
}
