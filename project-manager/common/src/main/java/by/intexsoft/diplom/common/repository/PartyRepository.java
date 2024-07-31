package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.PartyEntity;
import by.intexsoft.diplom.common.model.PersonModel;
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
}

