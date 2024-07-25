package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.PartyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyRateRepository extends JpaRepository<PartyRateModel, Integer> {

        @Query(value = "SELECT COUNT(*) as value FROM party_rate WHERE party_id = :partyId", nativeQuery = true)
        int countOfRatesByParty(@Param("partyId") PartyModel partyModel);

        Optional<List<PartyRateModel>> findAllByParty(PartyModel partyModel);
}
