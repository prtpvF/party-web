package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.role.PartyTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyTypeRepository extends JpaRepository<PartyTypeModel, Integer> {
    Optional<PartyTypeModel> findByType(String type);
}
