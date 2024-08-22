package by.intexsoft.diplom.person.util;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.person.dto.GuestDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ObjectMapper {

        public PartyDto convertPartyToDtoForOrganizer(PartyEntity partyEntity) {
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
                partyDto.setGuests(getConvertedToDtoGuestList(partyEntity));
                return partyDto;
        }

        public PartyDto convertPartyToDtoSlimVersion(PartyEntity partyEntity) {
                PartyDto partyDtoSlim = new PartyDto();
                partyDtoSlim.setId(partyEntity.getId());
                partyDtoSlim.setName(partyEntity.getName());
                partyDtoSlim.setOrganizerUsername(partyEntity.getOrganizer().getUsername());
                partyDtoSlim.setType(partyEntity.getType().getType());
                partyDtoSlim.setTicketCost(partyEntity.getTicketCost());
                return partyDtoSlim;
        }

        public List<PartyDto> getSlimPartyDtoListForOrganizer(List<PartyEntity> partyEntityList) {
                List<PartyDto> slimPartyDtoList = new ArrayList<>();
                for (PartyEntity partyEntity : partyEntityList) {
                        slimPartyDtoList.add(convertPartyToDtoSlimVersion(partyEntity));
                }
                return slimPartyDtoList;
        }

        public List<PartyDto> getPartyDtoListForOrganizer(List<PartyEntity> partyEntityList) {
                List<PartyDto> partyDtoList = new ArrayList<>();
                for (PartyEntity partyEntity : partyEntityList) {
                        partyDtoList.add(convertPartyToDtoForOrganizer(partyEntity));
                }
                return partyDtoList;
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
