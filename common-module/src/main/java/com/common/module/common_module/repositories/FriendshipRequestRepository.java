package com.common.module.common_module.repositories;

import com.common.module.common_module.models.FriendshipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Integer> {
}
