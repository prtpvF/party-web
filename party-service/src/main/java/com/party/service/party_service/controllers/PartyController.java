package com.party.service.party_service.controllers;

import com.party.service.party_service.domain.model.Party;
import com.party.service.party_service.repositorys.PartyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("party")
@RequiredArgsConstructor
public class PartyController {
    private final PartyRepo repo;

    @PostMapping("/create")
    public void createParty(Party party){
        repo.save(party);
    }
}
