package com.party.service.party_service.dto;

import com.party.service.party_service.domain.model.Party;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private int id;
    private String username;
    private int age;
    private double score;
    private Set<Party> parties;
}
