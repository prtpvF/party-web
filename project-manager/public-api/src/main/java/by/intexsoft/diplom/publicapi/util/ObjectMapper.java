package by.intexsoft.diplom.publicapi.util;

import by.intexsoft.diplom.common_module.model.Party.Party;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.publicapi.dto.PartyDto;
import by.intexsoft.diplom.publicapi.dto.PersonDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ObjectMapper {

    public PartyDto convertPartyToDto(Party party){
        PartyDto partyDto = new PartyDto();
        partyDto.setId(party.getId());
        partyDto.setCity(party.getCity());
        partyDto.setAddress(party.getAddress());
        partyDto.setImages(party.getImages());
        partyDto.setName(party.getName());
        partyDto.setDescription(party.getDescription());
        partyDto.setType(party.getType());
        partyDto.setAgeRestriction(party.getAgeRestriction());
        partyDto.setTicketCost(party.getTicketCost());
        partyDto.setDateOfEvent(party.getDateOfEvent());
        partyDto.setOrganizer(party.getOrganizer());
        partyDto.setCountOfPlaces(party.getCountOfPlaces());
      return partyDto;
    }

    public PersonDto convertPersonToDto(Person person){
        PersonDto personDto = new PersonDto();
        personDto.setAge(person.getAge());
        personDto.setFriends(person.getFriends());
        personDto.setRating(person.getRating());
        personDto.setUsername(person.getUsername());
        personDto.setCity(person.getCity());
        personDto.setId(person.getId());
        return personDto;
    }

    public List<PartyDto> convertPartyListToDto(List<Party> parties){
        List<PartyDto> mappedPartyList = parties.stream()
                .map(this::convertPartyToDto)
                .collect(Collectors.toList());
        return mappedPartyList;
    }



}
