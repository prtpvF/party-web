package by.intexsoft.diplom.draft.util;

import by.intexsoft.diplom.common.model.party.PartyDraftModel;
import by.intexsoft.diplom.draft.dto.PartyDraftDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ObjectMapper {

       public PartyDraftModel updatePartyDraft(PartyDraftDto updatedDraftDto, PartyDraftModel draftNeedToBeUpdated) {
           draftNeedToBeUpdated.setName(updatedDraftDto.getName());
           draftNeedToBeUpdated.setAgeRestriction(updatedDraftDto.getAgeRestriction());
           draftNeedToBeUpdated.setCountOfPlaces(updatedDraftDto.getCountOfPlaces());
           draftNeedToBeUpdated.setDescription(updatedDraftDto.getDescription());
           draftNeedToBeUpdated.setCity(updatedDraftDto.getCity());
           draftNeedToBeUpdated.setAddress(updatedDraftDto.getAddress());
           draftNeedToBeUpdated.setOwner(draftNeedToBeUpdated.getOwner());
           draftNeedToBeUpdated.setType(draftNeedToBeUpdated.getType());
           draftNeedToBeUpdated.setMinimalRating(updatedDraftDto.getMinimalRating());
           draftNeedToBeUpdated.setTicketCost(updatedDraftDto.getTicketCost());
           draftNeedToBeUpdated.setDateOfEvent(updatedDraftDto.getDateOfEvent());
           draftNeedToBeUpdated.setParty(draftNeedToBeUpdated.getParty());
           return draftNeedToBeUpdated;
       }

       public PartyDraftDto convertToDto(PartyDraftModel partyDraftModel) {
           PartyDraftDto draftDto = new PartyDraftDto();
           draftDto.setId(partyDraftModel.getId());
           draftDto.setName(partyDraftModel.getName());
           draftDto.setAgeRestriction(partyDraftModel.getAgeRestriction());
           draftDto.setCountOfPlaces(partyDraftModel.getCountOfPlaces());
           draftDto.setDescription(partyDraftModel.getDescription());
           draftDto.setCity(partyDraftModel.getCity());
           draftDto.setAddress(partyDraftModel.getAddress());
           draftDto.setTypeId(partyDraftModel.getType().getId());
           draftDto.setMinimalRating(partyDraftModel.getMinimalRating());
           draftDto.setTicketCost(partyDraftModel.getTicketCost());
           draftDto.setDateOfEvent(partyDraftModel.getDateOfEvent());
           if(partyDraftModel.getParty() != null) {
               draftDto.setPartyId(partyDraftModel.getParty().getId());
           }
           draftDto.setOwnerId(partyDraftModel.getOwner().getId());
          return draftDto;
       }

       public List<PartyDraftDto> convertListToDto(List<PartyDraftModel> partyDraftModels) {
           List<PartyDraftDto> partyDraftDtos = new ArrayList<>();
           for (PartyDraftModel partyDraftModel : partyDraftModels) {
               partyDraftDtos.add(convertToDto(partyDraftModel));
           }
           return partyDraftDtos;
       }

       public PartyDraftModel convertFromDto(PartyDraftDto partyDraftDto) {
           PartyDraftModel partyDraftModel = new PartyDraftModel();
           partyDraftModel.setCity(partyDraftDto.getCity());
           partyDraftModel.setAddress(partyDraftDto.getAddress());
           partyDraftModel.setDescription(partyDraftDto.getDescription());
           partyDraftModel.setName(partyDraftDto.getName());
           partyDraftModel.setMinimalRating(partyDraftDto.getMinimalRating());
           partyDraftModel.setCountOfPlaces(partyDraftDto.getCountOfPlaces());
           partyDraftModel.setDateOfEvent(partyDraftDto.getDateOfEvent());
           partyDraftModel.setTicketCost(partyDraftDto.getTicketCost());
           partyDraftModel.setAgeRestriction(partyDraftDto.getAgeRestriction());
           return partyDraftModel;
       }
}
