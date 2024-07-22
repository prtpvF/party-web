package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.role.PartyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyTypeRepository extends JpaRepository<PartyType, Integer> {
    Optional<PartyType> findByType(String type);
}
