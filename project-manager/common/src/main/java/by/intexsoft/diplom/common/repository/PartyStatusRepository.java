package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.PartyStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyStatusRepository extends JpaRepository<PartyStatusModel, Integer> {

        Optional<PartyStatusModel> findByStatus(String statusName);
}
