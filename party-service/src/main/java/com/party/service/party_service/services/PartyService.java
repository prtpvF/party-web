package com.party.service.party_service.services;

import com.common.module.common_module.repositories.PartyRepository;
import com.common.module.common_module.models.Party.Party;
import com.party.service.party_service.dto.PartyDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepo;
    private final RabbitTemplate rabbitTemplate;
    private final MessageConverter messageConverter;
    @Value("party.create.text")
    private String partyCreateText;

    public void createParty(PartyDTO partyDTO) {
        Party party = convertDtoToEntity(partyDTO);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.convertAndSend("createPartyQueue", partyCreateText);
        partyRepo.save(party);
    }

    private Party convertDtoToEntity(PartyDTO partyDTO) {
        Party party = new Party();
        party.setCity(partyDTO.getCity());
        party.setAddress(partyDTO.getAddress());
        party.setName(partyDTO.getName());
        party.setDescription(partyDTO.getDescription());
        party.setAgeRestriction(partyDTO.getAgeRestriction());
        party.setCreatedAt(partyDTO.getDateOfCreate());
        party.setDateOfEvent(partyDTO.getDateOfEvent());
        party.setOrganizer(partyDTO.getOrganizer());
        return party;
    }



}
