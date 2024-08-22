package by.intexsoft.diplom.chat.service;

import by.intexsoft.diplom.common.repository.conversation.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

        private final MessageRepository messageRepository;
        private final PersonService personService;
        
}
