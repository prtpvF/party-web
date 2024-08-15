package by.intexsoft.diplom.common.repository.conversation;

import by.intexsoft.diplom.common.model.conversation.ConversationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationModel, Integer> {
}
