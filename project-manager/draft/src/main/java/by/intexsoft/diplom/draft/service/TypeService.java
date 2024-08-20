package by.intexsoft.diplom.draft.service;

import by.intexsoft.diplom.common.model.enums.PartyDraftTypeEnum;
import by.intexsoft.diplom.common.model.draft.PartyUpdateDraftModel;
import by.intexsoft.diplom.common.model.party.PartyDraftType;
import by.intexsoft.diplom.common.repository.party.PartyDraftTypeRepository;
import by.intexsoft.diplom.draft.dto.PartyDraftDto;
import by.intexsoft.diplom.draft.exception.DraftTypeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TypeService {

        private final PartyDraftTypeRepository draftTypeRepository;

        public PartyDraftType findByName(String typeName) {
            return draftTypeRepository.findByName(typeName)
                    .orElseThrow(() -> new DraftTypeNotFoundException(
                            "Cannot find draft type"));
        }

        public PartyDraftType findById(Integer id) {
            return draftTypeRepository.findById(id)
                    .orElseThrow(() -> new DraftTypeNotFoundException(
                            "Cannot find draft type"));
        }

        public void assignDraftType(PartyDraftDto partyDraftDto, PartyUpdateDraftModel draftModel) {
            if (partyDraftDto.getPartyId() == null) {
                draftModel.setType(findByName(PartyDraftTypeEnum.CREATING.name()));
            } else {
                draftModel.setType(findByName(PartyDraftTypeEnum.UPDATING.name()));
            }
        }
}