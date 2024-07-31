package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.DeletingPartyRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeletingPartyRequestRepository extends JpaRepository<DeletingPartyRequestModel, Integer> {
    Optional<DeletingPartyRequestModel> findByPartyId(Integer partyId);
}
