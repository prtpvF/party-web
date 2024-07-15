package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
}
