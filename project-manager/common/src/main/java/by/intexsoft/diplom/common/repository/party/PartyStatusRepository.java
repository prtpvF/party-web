package by.intexsoft.diplom.common.repository.party;

import by.intexsoft.diplom.common.model.status.PartyStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyStatusRepository extends JpaRepository<PartyStatusModel, Integer> {

        Optional<PartyStatusModel> findByStatus(String statusName);
}
