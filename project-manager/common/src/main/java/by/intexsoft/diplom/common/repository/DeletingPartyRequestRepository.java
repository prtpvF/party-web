package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.DeletingPartyRequestMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeletingPartyRequestRepository extends JpaRepository<DeletingPartyRequestMode, Integer> {
    Optional<DeletingPartyRequestMode> findByPartyId(Integer partyId);
}
