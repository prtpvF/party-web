package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.models.Party.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends JpaRepository<Party, Integer> {
}
