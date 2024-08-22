package by.intexsoft.diplom.common.repository.party;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Integer> {

        List<PartyEntity> findAllByOrganizer(PersonModel personModel);

        @Query(value = "SELECT * FROM Party p WHERE p.city=:city AND p.status_id != 1", nativeQuery = true)
        List<PartyEntity> findAllUnavailableByCity(@Param("city") String city);

        @Query(value = "SELECT * FROM Party p WHERE p.city=city AND p.status_id=status_id", nativeQuery = true)
        Page<PartyEntity> findAllByStatusAndCity(Integer statusId, String city, Pageable pageable);


        @Query(value = "SELECT p FROM PartyEntity p WHERE p.city=:city AND p.status.id=1")
        Page<PartyEntity> findAllAvailableByCity(@Param("city") String city, Pageable pageable);

        @Query(value = "SELECT p FROM PartyEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.status.id=1")
        Page<PartyEntity> findAllAvailableByName(@Param("name") String name, Pageable pageable);

        @Query(value = "SELECT p FROM PartyEntity p WHERE p.type.type=:type AND p.status.id=1")
        Page<PartyEntity> findAllAvailableByType(@Param("type") String type, Pageable pageable);
}

