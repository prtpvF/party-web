package by.intexsoft.diplom.common.repository.party;

import by.intexsoft.diplom.common.model.status.PartyDraftStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyDraftStatusRepository extends JpaRepository<PartyDraftStatusModel, Integer> {

        Optional<PartyDraftStatusModel> findByName(String name);
}
