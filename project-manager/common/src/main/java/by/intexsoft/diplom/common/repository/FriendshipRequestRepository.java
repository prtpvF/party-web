package by.intexsoft.diplom.common.repository;

import by.intexsoft.diplom.common.model.FriendshipRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequestModel, Integer> {
}
