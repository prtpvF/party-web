package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.ParticipationRequestModel;
import by.intexsoft.diplom.common.model.PartyModel;
import by.intexsoft.diplom.common.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequestModel, Integer> {

        @Query(value = "select * from participation_request pr where pr.person_id = person_id AND pr.party_id = party_id",
                nativeQuery = true)
        Optional<ParticipationRequestModel> findByPersonAndParty(PersonModel personModel, PartyModel partyModel);
}
