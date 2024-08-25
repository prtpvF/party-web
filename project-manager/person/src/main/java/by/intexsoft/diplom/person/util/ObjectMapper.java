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

        public PartyDto convertToSlimDto(PartyEntity partyEntity) {
                PartyDto partyDtoSlim = new PartyDto();
                partyDtoSlim.setId(partyEntity.getId());
                partyDtoSlim.setName(partyEntity.getName());
                partyDtoSlim.setOrganizerUsername(partyEntity.getOrganizer().getUsername());
                partyDtoSlim.setType(partyEntity.getType().getType());
                partyDtoSlim.setTicketCost(partyEntity.getTicketCost());
                return partyDtoSlim;
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
