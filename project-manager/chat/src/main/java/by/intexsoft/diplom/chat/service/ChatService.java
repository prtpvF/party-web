package by.intexsoft.diplom.chat.service;

import by.intexsoft.diplom.chat.dto.ChatMessageDto;
import by.intexsoft.diplom.common.model.conversation.MessageModel;
import by.intexsoft.diplom.common.repository.conversation.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

        private final MessageRepository messageRepository;
        private final ModelMapper modelMapper;

        public void saveMessage(MessageModel messageModel) {
            messageRepository.save(messageModel);
        }

        public ChatMessageDto convertToDto(MessageModel messageModel) {
            ChatMessageDto chatMessageDto = new ChatMessageDto();
            modelMapper.map(messageModel, chatMessageDto);
        }

        private void mapAdditionalFieldToMessageDto(MessageModel messageModel,
                                                    ChatMessageDto chatMessageDto) {}
}
