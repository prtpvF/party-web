package com.party.service.party_service.controllers;

import com.party.service.party_service.domain.model.Party;
import com.party.service.party_service.dto.PartyDTO;
import com.party.service.party_service.repositorys.PartyRepo;
import com.party.service.party_service.services.PartyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("party")
@RequiredArgsConstructor
public class PartyController {
    private final PartyService partyService;

    @PostMapping("/create")
    public void createParty(@RequestBody @Valid PartyDTO partyDTO){
        partyService.createParty(partyDTO);

    }
}
