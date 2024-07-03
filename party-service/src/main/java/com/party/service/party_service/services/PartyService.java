package com.party.service.party_service.services;

import com.party.service.party_service.repositorys.PartyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepo partyRepo;

    public void createParty(){

    }

}
