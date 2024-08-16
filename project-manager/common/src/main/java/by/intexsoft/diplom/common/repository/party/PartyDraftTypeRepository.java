package by.intexsoft.diplom.common.repository.party;

import by.intexsoft.diplom.common.model.party.PartyDraftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyDraftTypeRepository extends JpaRepository<PartyDraftType, Integer> {

        Optional<PartyDraftType> findByName(String name);
}
