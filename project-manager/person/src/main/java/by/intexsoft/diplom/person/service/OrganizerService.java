package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common.model.enums.ParticipationRequestStatusEnum;
import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.person.dto.GuestDto;
import by.intexsoft.diplom.person.dto.OrgAnswerDto;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.exception.*;
import by.intexsoft.diplom.person.util.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizerService {

        private final PersonService personService;
        private final PartyService partyService;
        private final NotificationService notificationService;
        private final ParticipationRequestService requestService;
        private final PersonRepository personRepository;
        private final ModelMapper modelMapper;
        private final ObjectMapper objectMapper;

        /**
         * method checks is organizer's decision positive or not.
         * If organizer has accepted a request - person will be added to party's guests list
         * and will receive a notification about it
         * or else the request will be deleted and person will receive a notification
         * about refusal
         * @param requestId  identifier of participation request
         * @param principal  authenticated user
         * @param orgAnswerDto  Dto of organizer's decision flag (accept if flag = true, refuse if flag = false)
         * @return HttpStatus
         */
        public HttpStatus answerRequest(int requestId,
                                        Principal principal,
                                        OrgAnswerDto orgAnswerDto) {
            checkPersonRole(principal);
            ParticipationRequestModel request = requestService.findRequestById(requestId);
            ParticipationRequestDto dto = modelMapper.map(request,
                                          ParticipationRequestDto.class);
            PersonModel organizer = personService.getPersonByPrincipal(principal);

            if(Boolean.TRUE.equals(orgAnswerDto.getAccept())){
                isRequestBelongToOrganizer(dto, organizer);
                PartyEntity party = partyService.retrievePartyFromRequest(dto);
                PersonModel guest = retrievePersonFromRequest(dto);
                partyService.addPersonToPartyGuest(guest, party);
                notificationService.sendNotificationAboutParticipationRequest(request,true);
                requestService.setStatusToRequestAndSave(request,
                        ParticipationRequestStatusEnum.IN_PROCESS.name());
                personRepository.save(guest);
                return HttpStatus.CREATED;
            }
            notificationService.sendNotificationAboutParticipationRequest(request,false);
            requestService.setStatusToRequestAndSave(request,
                    ParticipationRequestStatusEnum.REJECTED.name());
            return HttpStatus.OK;
        }

        /**
         * Returns a slim DTO object with: id, name, ticket cost, organizer, type fields
         * @param principal - authenticated organizer
         * @param pageable - pagination information
         * @return Page of slim organizer's DTO
         */
        public Page<PartyDto> getMyParties(Principal principal, Pageable pageable) {
            checkPersonRole(principal);
            PersonModel organizer = personService.getPersonByPrincipal(principal);
            Page<PartyEntity> partyEntities = partyService.findAllOrganizerParties(organizer, pageable);
            return partyEntities.map(objectMapper::convertPartyToSlimDto);
        }

        public Page<GuestDto> getPartyGuest(Integer partyId,
                                            Pageable pageable,
                                            Principal principal) {
            if(principal != null) {
                PersonModel organizer = personService.getPersonByPrincipal(principal);
                PartyEntity party = partyService.findPartyById(partyId);
                isPartyBelongToOrganizer(organizer, party);
                return personService.getAllPartyGuest(partyId, pageable);
            }
            else {
                throw new UnauthorizedException("You are not logged in");
            }
        }

        public Page<ParticipationRequestDto> getAllRequestByParty(Integer partyId,
                                                                  Principal principal,
                                                                  Pageable pageable) {
            PersonModel organizer = personService.getPersonByPrincipal(principal);
            PartyEntity party = partyService.findPartyById(partyId);
            isPartyBelongToOrganizer(organizer, party);
            return requestService.findByPartyAndStatus(party.getId(), pageable);
        }

        private void isRequestBelongToOrganizer(ParticipationRequestDto request,
                                                PersonModel organizer) {
            PartyEntity partyFromRequest = partyService.findPartyById(request.getPartyDto().getId());
            if(!partyFromRequest.getOrganizer().equals(organizer)){
               throw new InvalidRequestOwner("you cant answer this request " +
                       "'cause you are not the organizer of this party");
            }
        }

        /**
         * method retrieves person model from participation request
         * @param request - ParticipationRequest object
         * @return founded person
         */
        private PersonModel retrievePersonFromRequest(ParticipationRequestDto request) {
            return personService.findPersonById(request.getGuestId());
        }

        private void checkPersonRole(Principal principal) {
            PersonModel organizer = personService.getPersonByPrincipal(principal);

            if(!organizer.getRole()
                    .getRoleName()
                    .equals(PersonRolesEnum.ORGANIZER.name())) {
                throw new AccessDeniedException("You cannot enrich this functionality");
            }
        }

        private void isPartyBelongToOrganizer(PersonModel organizer,
                                              PartyEntity party) {
            if(!party.getOrganizer().equals(organizer)) {
                throw new IllegalPartyOrganizerException(
                        "you are not owner of this party");
            }
        }
}