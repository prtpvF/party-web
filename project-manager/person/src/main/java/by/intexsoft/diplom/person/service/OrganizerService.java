package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.person.dto.OrgAnswerDto;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.exception.IllegalPartyOrganizerException;
import by.intexsoft.diplom.person.exception.InvalidRequestOwner;
import by.intexsoft.diplom.person.util.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


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
                personRepository.save(guest);
                return HttpStatus.CREATED;
            }
            notificationService.sendNotificationAboutParticipationRequest(request,false);
            return HttpStatus.OK;
        }

        /**
         * returns slim dto object with: id, name, ticket cost, organizer, type fields
         * @param principal - authenticated organizer
         * @return list of slim organizer's dto
         */
        public List<PartyDto> getMyParties(Principal principal) {
            PersonModel organizer = personService.getPersonByPrincipal(principal);
            return objectMapper.getSlimPartyDtoListForOrganizer(
                    partyService
                            .findAllOrganizerParties(organizer));
        }

        public PartyDto getParty(Integer partyId, Principal principal) {
            PartyEntity party = partyService.findPartyById(partyId);
            isPartyBelongsToOrganizer(principal, party);
            return objectMapper.convertPartyToDtoForOrganizer(party);
        }

        private void isRequestBelongToOrganizer(ParticipationRequestDto request,
                                                PersonModel organizer) {
            PartyEntity partyFromRequest = personService.findPartyById(request.getPartyId());
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
            return personService.findPersonById(request.getPersonId());
        }

        private void isPartyBelongsToOrganizer(Principal principal,
                                               PartyEntity party) {
            PersonModel organizer = personService.getPersonByPrincipal(principal);
            if(!party.getOrganizer().equals(organizer)) {
                throw new IllegalPartyOrganizerException(
                        "you are not the organizer of this party!");
            }
        }
}