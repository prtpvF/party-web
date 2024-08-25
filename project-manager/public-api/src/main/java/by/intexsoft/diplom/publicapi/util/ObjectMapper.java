package by.intexsoft.diplom.publicapi.util;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.publicapi.dto.GuestDto;
import by.intexsoft.diplom.publicapi.dto.PartyDto;
import by.intexsoft.diplom.publicapi.dto.PersonDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ObjectMapper {

        public PartyDto convertPartyToDtoForOrganizer(PartyEntity partyEntity) {
            PartyDto partyDto = convertPartyToDto(partyEntity);
            partyDto.setGuests(getConvertedToDtoGuestList(partyEntity));
            return partyDto;
        }

        public PartyDto convertPartyToDto(PartyEntity partyEntity) {
            PartyDto partyDto = new PartyDto();
            partyDto.setId(partyEntity.getId());
            partyDto.setName(partyEntity.getName());
            partyDto.setCoordinates(partyEntity.getCoordinates());
            partyDto.setAgeRestriction(partyEntity.getAgeRestriction());
            partyDto.setCountOfPlaces(partyEntity.getCountOfPlaces());
            partyDto.setOrganizerUsername(partyEntity.getOrganizer().getUsername());
            partyDto.setDateOfEvent(partyEntity.getDateOfEvent());
            partyDto.setDescription(partyEntity.getDescription());
            partyDto.setMinimalRating(partyEntity.getMinimalRating());
            partyDto.setTicketCost(partyEntity.getTicketCost());
            partyDto.setType(partyEntity.getType().getType());
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

        private Set<GuestDto> getConvertedToDtoGuestList(PartyEntity partyEntity) {
            Set<GuestDto> guestDtoList = new HashSet<>();

            for(PersonModel personModel : partyEntity.getGuests()) {
                guestDtoList.add(convertGuestToDto(personModel));
            }
            return guestDtoList;
        }

        private GuestDto convertGuestToDto(PersonModel personModel) {
            GuestDto guestDto = new GuestDto();
            guestDto.setId(personModel.getId());
            guestDto.setAge(personModel.getAge());
            guestDto.setUsername(personModel.getUsername());
            guestDto.setRating(personModel.getRating());
            return guestDto;
        }
}