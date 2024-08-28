package by.intexsoft.diplom.common.repository.request;

import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequestModel, Integer> {

        @Query(value = "select pr from ParticipationRequestModel pr where pr.person =:person AND pr.party=:party")
        Optional<ParticipationRequestModel> findByPersonAndParty(@Param("person") PersonModel person,
                                                                 @Param("party") PartyEntity party);

        @Query("SELECT p FROM ParticipationRequestModel p where p.party.id=:partyId")
        Page<ParticipationRequestModel> findAllByParty(@Param("partyId") Integer partyId, Pageable pageable);

        @Query("SELECT p FROM ParticipationRequestModel p where p.person.id=:personId")
        Page<ParticipationRequestModel> findAllByPerson(@Param("personId") Integer personId, Pageable pageable);
}
