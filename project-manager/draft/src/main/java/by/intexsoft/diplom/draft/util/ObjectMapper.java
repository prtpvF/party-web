package by.intexsoft.diplom.draft.util;

<<<<<<< HEAD
import by.intexsoft.diplom.common.model.draft.PartyCreateDraftModel;
import by.intexsoft.diplom.common.model.draft.PartyDraftAbstract;
import by.intexsoft.diplom.common.model.draft.PartyUpdateDraftModel;
import by.intexsoft.diplom.draft.dto.PartyDraftDto;
import org.springframework.stereotype.Service;

@Service
public class ObjectMapper<T extends PartyDraftAbstract> {

       public T convertFromDto(PartyDraftDto dto, T draftModel) {
           draftModel.setAddress(dto.getAddress());
           draftModel.setAgeRestriction(dto.getAgeRestriction());
           draftModel.setCity(dto.getCity());
           draftModel.setCountOfPlaces(dto.getCountOfPlaces());
           draftModel.setDateOfEvent(dto.getDateOfEvent());
           draftModel.setDescription(dto.getDescription());
           draftModel.setMinimalRating(dto.getMinimalRating());
           draftModel.setTicketCost(dto.getTicketCost());
           return (T) draftModel;
       }

       public PartyDraftDto convertCreatingDraftToDto(PartyCreateDraftModel draft) {
           PartyDraftDto dto = getPartyDraftDto((T) draft);
           return dto;
       }

        public PartyDraftDto convertUpdatingDraftToDto(PartyUpdateDraftModel draft) {
            PartyDraftDto dto = getPartyDraftDto((T) draft);
            dto.setPartyId(draft.getParty().getId());
            return dto;
        }

        private PartyDraftDto getPartyDraftDto(T draft) {
            PartyDraftDto dto = new PartyDraftDto();
            dto.setAddress(draft.getAddress());
            dto.setAgeRestriction(draft.getAgeRestriction());
            dto.setCity(draft.getCity());
            dto.setCountOfPlaces(draft.getCountOfPlaces());
            dto.setDateOfEvent(draft.getDateOfEvent());
            dto.setDescription(draft.getDescription());
            dto.setMinimalRating(draft.getMinimalRating());
            dto.setTicketCost(draft.getTicketCost());
            dto.setOwnerId(draft.getOwner().getId());
            return dto;
        }
}
=======
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
>>>>>>> 3fde4c30443d2041b900952d15a715083e9858cf
