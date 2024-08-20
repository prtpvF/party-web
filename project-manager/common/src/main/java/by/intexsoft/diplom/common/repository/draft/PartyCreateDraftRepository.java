package by.intexsoft.diplom.common.repository.draft;

import by.intexsoft.diplom.common.model.draft.PartyCreateDraftModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyCreateDraftRepository extends JpaRepository<PartyCreateDraftModel, Integer> {
}
