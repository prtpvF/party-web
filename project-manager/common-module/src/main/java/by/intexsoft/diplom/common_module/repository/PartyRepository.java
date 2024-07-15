package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.Party.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<Party, Integer> {
    List<Party> findAllByCity(String city);
}
