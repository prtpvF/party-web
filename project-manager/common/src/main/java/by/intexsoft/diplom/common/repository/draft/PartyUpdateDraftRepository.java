package by.intexsoft.diplom.common.repository.draft;

import by.intexsoft.diplom.common.model.draft.PartyUpdateDraftModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyUpdateDraftRepository extends JpaRepository<PartyUpdateDraftModel, Integer> {

        Optional<PartyUpdateDraftModel> findByPartyId(Integer partyId);
}
