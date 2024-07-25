package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, Integer> {
}
