package by.intexsoft.diplom.common_module.repository;

import by.intexsoft.diplom.common_module.model.DeletingPartyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeletingPartyRequestRepository extends JpaRepository<DeletingPartyRequest, Integer> {
    Optional<DeletingPartyRequest> findByPartyId(Integer partyId);
}
