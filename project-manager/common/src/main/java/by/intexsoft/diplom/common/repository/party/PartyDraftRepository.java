package by.intexsoft.diplom.common.repository.party;

import by.intexsoft.diplom.common.model.party.PartyDraftModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyDraftRepository extends JpaRepository<PartyDraftModel, Integer> {
}
