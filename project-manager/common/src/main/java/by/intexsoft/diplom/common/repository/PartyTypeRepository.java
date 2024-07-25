package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.role.PartyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyTypeRepository extends JpaRepository<PartyType, Integer> {
    Optional<PartyType> findByType(String type);
}
