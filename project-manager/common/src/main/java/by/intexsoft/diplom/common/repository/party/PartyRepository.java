package by.intexsoft.diplom.common.repository.party;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Integer> {

        @Query(value = "SELECT * FROM Party p WHERE p.city=city AND p.status_id=1", nativeQuery = true)
        List<PartyEntity> findAllAvailableByCity(String city);

        @Query(value = "SELECT * FROM Party p WHERE p.person_id=person_id", nativeQuery = true)
        List<PartyEntity> findAllByPerson(PersonModel personModel);

        @Query(value = "SELECT * FROM Party p WHERE p.city=city AND p.status_id=2", nativeQuery = true)
        List<PartyEntity> findAllUnavailableByCity(String city);

        @Query(value = "SELECT * FROM Party p WHERE p.city=city AND p.status_id=status_id", nativeQuery = true)
        List<PartyEntity> findAllByStatusAndCity(Integer statusId, String city);
}

