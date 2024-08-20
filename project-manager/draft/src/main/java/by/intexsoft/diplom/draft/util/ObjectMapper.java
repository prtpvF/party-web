package by.intexsoft.diplom.draft.util;

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