package by.intexsoft.diplom.draft.service.draft;

import by.intexsoft.diplom.common.model.draft.PartyCreateDraftModel;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.draft.PartyCreateDraftRepository;
import by.intexsoft.diplom.draft.dto.PartyDraftDto;
import by.intexsoft.diplom.draft.exception.DraftNotFoundException;
import by.intexsoft.diplom.draft.service.PersonService;
import by.intexsoft.diplom.draft.service.StatusService;
import by.intexsoft.diplom.draft.service.TypeService;
import by.intexsoft.diplom.draft.util.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Implementation fot creating drafts
 * @version 1.0
 * @author Mihail Chaplygin
 */
@Service
public class CreatingDraftService extends AbstractPartyDraftService<PartyCreateDraftModel>{

        private final PartyCreateDraftRepository partyCreateDraftRepository;
        private final PersonService personService;
        private final ModelMapper modelMapper;
        private final StatusService statusService;
        private final TypeService typeService;
        private final ObjectMapper objectMapper;

        protected CreatingDraftService(PartyCreateDraftRepository partyCreateDraftRepository,
                                       PersonService personService,
                                       ModelMapper modelMapper,
                                       StatusService statusService,
                                       TypeService typeService,
                                       ObjectMapper objectMapper) {
            super(modelMapper,
                    statusService,
                    typeService);
            this.partyCreateDraftRepository = partyCreateDraftRepository;
            this.personService = personService;
            this.modelMapper = modelMapper;
            this.statusService = statusService;
            this.typeService = typeService;
            this.objectMapper = objectMapper;
        }

        @Override
        public HttpStatus createDraft(PartyDraftDto dto, Principal principal) {
            PersonModel personModel = personService.findByPrincipal(principal);
            PartyCreateDraftModel draft = modelMapper.map(dto, PartyCreateDraftModel.class);
            super.mapAdditionalFields(dto, draft, personModel);
            partyCreateDraftRepository.save(draft);
            return CREATED;
        }

        @Override
        public PartyDraftDto getDraftById(Principal Principal, Integer draftId) {
            PersonModel personModel = personService.findByPrincipal(Principal);
            PartyCreateDraftModel draft = getDraftById(draftId);
            super.isDraftBelongToPerson(personModel, draft);
            return objectMapper.convertCreatingDraftToDto(draft);
        }

        @Override
        public HttpStatus updateDraft(Principal principal,
                                      PartyDraftDto partyDraftDto,
                                      Integer draftNeedToBeUpdatedId) {
            PersonModel personModel = personService.findByPrincipal(principal);
            PartyCreateDraftModel draftNeedToBeUpdate = getDraftById(draftNeedToBeUpdatedId);
            modelMapper.map(partyDraftDto, draftNeedToBeUpdate);
            draftNeedToBeUpdate.setId(draftNeedToBeUpdate.getId());
            draftNeedToBeUpdate.setOwner(draftNeedToBeUpdate.getOwner());
            super.isDraftBelongToPerson(personModel, draftNeedToBeUpdate);
            partyCreateDraftRepository.save(draftNeedToBeUpdate);
            return OK;
        }

        @Override
        public HttpStatus deleteDraft(Principal principal,
                                      Integer draftId) {
            partyCreateDraftRepository.delete(getDraftById(draftId));
            return OK;
        }

        public List<PartyDraftDto> getAllDtoDrafts(Principal principal) {
            PersonModel personModel = personService.findByPrincipal(principal);
            return super.getAllDrafts(personModel);
        }

        private PartyCreateDraftModel getDraftById(Integer draftId) {
            PartyCreateDraftModel draft = partyCreateDraftRepository.findById(draftId)
                    .orElseThrow(() -> new DraftNotFoundException("cannot find draft with this id"));
            return draft;
        }
}