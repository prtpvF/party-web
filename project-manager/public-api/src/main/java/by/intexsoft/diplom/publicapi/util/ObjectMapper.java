package by.intexsoft.diplom.publicapi.util;

import by.intexsoft.diplom.common.model.party.ImageModel;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.publicapi.dto.PartyDto;
import by.intexsoft.diplom.publicapi.dto.PersonDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectMapper {

        public PartyDto convertPartyToDto(PartyEntity party){
            PartyDto partyDto = new PartyDto();
           // partyDto.setId(party.getId());
            partyDto.setCity(party.getCity());
            partyDto.setAddress(party.getCoordinates());
           // partyDto.setImageIdList(getImageIdList(party));
            partyDto.setName(party.getName());
            partyDto.setDescription(party.getDescription());
            partyDto.setTypeId(party.getType().getId());
            partyDto.setAgeRestriction(party.getAgeRestriction());
            partyDto.setTicketCost(party.getTicketCost());
            partyDto.setDateOfEvent(party.getDateOfEvent());
            partyDto.setOrganizer(party.getOrganizer().getId());
            partyDto.setCountOfPlaces(party.getCountOfPlaces());
          return partyDto;
        }

        public PersonDto convertPersonToDto(PersonModel person){
            PersonDto personDto = new PersonDto();
            personDto.setAge(person.getAge());
            personDto.setFriends(person.getFriends());
            personDto.setRating(person.getRating());
            personDto.setUsername(person.getUsername());
            personDto.setCity(person.getCity());
            personDto.setId(person.getId());
            return personDto;
        }

        public List<PartyDto> convertPartyListToDto(List<PartyEntity> parties){
            List<PartyDto> mappedPartyList = parties.stream()
                    .map(this::convertPartyToDto)
                    .collect(Collectors.toList());
            return mappedPartyList;
        }

        private Set<Integer> getImageIdList(PartyEntity party) {
            Set<Integer> imageIdList = new HashSet<>();
            for (ImageModel image : party.getImagePath()) {
                imageIdList.add(image.getId());
            }
            return imageIdList;
        }
}