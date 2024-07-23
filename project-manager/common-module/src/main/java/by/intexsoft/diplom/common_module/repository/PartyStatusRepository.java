package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.PartyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyStatusRepository extends JpaRepository<PartyStatus, Integer> {

        Optional<PartyStatus> findByStatus(String statusName);
}
