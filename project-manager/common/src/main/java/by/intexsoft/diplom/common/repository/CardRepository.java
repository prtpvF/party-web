package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.CardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<CardModel, Integer> {
}
