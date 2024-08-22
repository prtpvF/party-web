package by.intexsoft.diplom.admin.util;

import by.intexsoft.diplom.admin.dto.PartyDto;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ObjectMapper {

        public PartyDto convertPartyToDto(PartyEntity party) {
            PartyDto dto = new PartyDto();
            dto.setId(party.getId());
            dto.setName(party.getName());
            dto.setMinimalRating(party.getMinimalRating());
            dto.setAddress(party.getCoordinates());
            dto.setCity(party.getCity());
            dto.setAgeRestriction(party.getAgeRestriction());
            dto.setImageName(party.getImagePath());
            dto.setDescription(party.getDescription());
            dto.setDateOfEvent(party.getDateOfEvent());
            dto.setType(party.getType().getType());
            dto.setTicketCost(party.getTicketCost());
            dto.setCountOfPlaces(party.getCountOfPlaces());
            return dto;
        }

        public List<PartyDto> convertPartyListToDtoList(List<PartyEntity> partyList) {
            List<PartyDto> dtoList = new ArrayList<>();
            for (PartyEntity party : partyList) {
                dtoList.add(convertPartyToDto(party));
            }
            return dtoList;
        }
}
