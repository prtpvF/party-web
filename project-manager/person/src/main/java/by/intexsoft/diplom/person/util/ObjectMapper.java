package by.intexsoft.diplom.person.util;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapper {

        public PartyDto convertPartyToSlimDto(PartyEntity partyEntity) {
                PartyDto partyDtoSlim = new PartyDto();
                partyDtoSlim.setId(partyEntity.getId());
                partyDtoSlim.setName(partyEntity.getName());
                partyDtoSlim.setOrganizerUsername(partyEntity.getOrganizer().getUsername());
                partyDtoSlim.setType(partyEntity.getType().getType());
                partyDtoSlim.setTicketCost(partyEntity.getTicketCost());
                return partyDtoSlim;
        }

        public ParticipationRequestDto convertRequestToDto(ParticipationRequestModel requestModel) {
                ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
                participationRequestDto.setId(requestModel.getId());
                participationRequestDto.setStatusName(requestModel.getStatus().getStatusName());
                participationRequestDto.setPartyDto(convertPartyToSlimDto(requestModel.getParty()));
                participationRequestDto.setOrganizerUsername(requestModel
                        .getParty()
                        .getOrganizer()
                        .getUsername());
                return participationRequestDto;
        }


}
