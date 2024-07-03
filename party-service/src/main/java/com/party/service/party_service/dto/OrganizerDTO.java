package com.party.service.party_service.dto;

import com.party.service.party_service.domain.model.Party;
import lombok.*;

import java.util.List;
import java.util.UUID;


public class OrganizerDTO {

    private UUID id;
   private List<UUID> allOrgParty;
    private String username;
    private String image;
}
