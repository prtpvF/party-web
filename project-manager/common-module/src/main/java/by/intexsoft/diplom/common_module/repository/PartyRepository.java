package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<Party, Integer> {

    @Query(value = "SELECT * FROM Party p WHERE p.city=city AND p.status_id=1", nativeQuery = true)
    List<Party> findAllAvailableByCity(String city);
}

