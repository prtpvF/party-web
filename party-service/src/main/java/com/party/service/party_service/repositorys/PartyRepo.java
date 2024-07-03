package com.party.service.party_service.repositorys;

import com.party.service.party_service.domain.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PartyRepo extends JpaRepository<Party, UUID> {
}
