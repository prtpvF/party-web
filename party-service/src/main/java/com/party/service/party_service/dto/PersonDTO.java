package com.party.service.party_service.configuration.dto;

import com.party.service.party_service.model.Party;

import java.util.Set;

public class PersonDTO {
    private int id;
    private String username;
    private int age;
    private double score;
    private Set<Party> parties;
}
