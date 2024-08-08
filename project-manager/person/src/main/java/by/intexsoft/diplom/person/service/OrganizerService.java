package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common.model.*;
import by.intexsoft.diplom.common.model.enums.PartyStatusEnum;
import by.intexsoft.diplom.common.repository.*;
import by.intexsoft.diplom.person.dto.OrgAnswerDto;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizerService {

        private final PersonService personService;
        private final PartyRepository partyRepository;
        private final PersonRepository personRepository;
        private final PartyStatusRepository partyStatusRepository;
        private final DeletingPartyRequestRepository deletingRequestRepository;
        private final ParticipationRequestRepository requestRepository;
        private final ModelMapper modelMapper;

        /**
         * method creates a request to create party
         * by retrieving and converting PartyDto in Party model
         * @param principal - authenticated user
         * @param partyCreateDto - object what will be converted in Party model
         * @return HttpStatus
         */
        public HttpStatus createPartyRequest(Principal principal,
                                             PartyDto partyCreateDto) {
            PersonModel organizer = personService.getPersonFromToken(principal);
            PartyEntity party = convertToParty(partyCreateDto, organizer);
            checkEventDate(partyCreateDto);
            partyRepository.save(party);
            return HttpStatus.CREATED;
        }

        /**
         * method creates a request to delete a party
         * by retrieving Party model from DB and calls validating method
         * @param principal - authenticated user
         * @param partyId - identifier of an instance of Party what will be retrieved from DB
         * @return HttpStatus
         */
        public HttpStatus createPartyDeleteRequest(int partyId, Principal principal) {
            DeletingPartyRequestModel request = new DeletingPartyRequestModel();
            prepareDeletingRequest(request, principal, partyId);
            deletingRequestRepository.save(request);
            return HttpStatus.OK;
        }

        public HttpStatus updateParty(int partyId,
                                      Principal principal,
                                      PartyDto partyDto) {
            PartyEntity party = personService.findPartyById(partyId);
            checkPartyOwner(principal, partyId);
            modelMapper.map(partyDto, party);
            System.out.println(party);
            partyRepository.save(party);
            return HttpStatus.OK;
        }

        /**
         * method checks is organizer's decision positive or not.
         * If organizer has accepted a request - person will be added to party's guests list
         * and will receive a notification about it
         * or else the request will be deleted and person will receive a notification
         * about refusal
         * @param requestId - identifier of participation request
         * @param principal - authenticated user
         * @param orgAnswerDto - Dto of organizer's decision flag (accept if flag = true, refuse if flag = false)
         * @return HttpStatus
         */
        public HttpStatus answerRequest(int requestId,
                                        Principal principal,
                                        OrgAnswerDto orgAnswerDto) {
            ParticipationRequestModel request = findRequestById(requestId);
            ParticipationRequestDto dto = modelMapper.map(request, ParticipationRequestDto.class);
            PersonModel organizer = personService.getPersonFromToken(principal);
            if(orgAnswerDto.getAccept()){
                isRequestBelongToOrganizer(dto, organizer);
                PartyEntity party = retrievePartyFromRequest(dto);
                PersonModel guest = retrievePersonFromRequest(dto);
                addPersonToPartyGuest(guest, party);
                //todo send notification about accepting
                return HttpStatus.CREATED;
            }
            //todo send notification about rejecting
            return HttpStatus.OK;
        }

        public HttpStatus deleteRequest(int requestId, Principal principal) {
            PersonModel organizer = personService.getPersonFromToken(principal);
            ParticipationRequestDto requestDto = personService.getValidatedPersonParticipationRequest(requestId);
            isRequestBelongToOrganizer(requestDto, organizer);
            requestRepository.deleteById(requestId);
            return HttpStatus.OK;
        }

        private void addPersonToPartyGuest(PersonModel person, PartyEntity party) {
            party.getGuests().add(person);
            person.getParties().add(party);
            partyRepository.save(party);
            personRepository.save(person);
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
         * method retrieves party model from participation request
         * @param request - ParticipationRequestDto object
         * @return founded party
         */
        private PartyEntity retrievePartyFromRequest(ParticipationRequestDto request){
            return personService.findPartyById(request.getPartyId());
        }

        /**
         * method retrieves person model from participation request
         * @param request - ParticipationRequest object
         * @return founded person
         */
        private PersonModel retrievePersonFromRequest(ParticipationRequestDto request) {
            return personService.findPersonById(request.getPersonId());
        }

        /**
         * method find a participation request by id in DB
         * or throw an exception if nothing found
         * @param requestId - identifier of participate request
         * @return founded participate request
         */
        private ParticipationRequestModel findRequestById(int requestId) {
            return requestRepository.findById(requestId)
                    .orElseThrow(() -> new RequestNotFoundException
                            ("participate request with this id doesnt exist"));
        }

        private void prepareDeletingRequest(DeletingPartyRequestModel request,
                                            Principal principal,
                                            int partyId) {
            request.setOrganizer(personService.getPersonFromToken(principal));
            request.setParty(personService.findPartyById(partyId));
            checkPartyOwner(principal, partyId);
            isDeletingRequestExist(partyId);
        }

        /**
         * This method checks whether the organizer retrieved from the token
         * corresponds to the true party’s organizer.
         * @param principal - authenticated user
         * @param partyId - identification of a party
         */
        private void checkPartyOwner(Principal principal, int partyId) {
            PartyEntity party = personService.findPartyById(partyId);
            PersonModel organizer = personService.getPersonFromToken(principal);
            if(!party.getOrganizer().equals(organizer)){
                throw new IllegalPartyOrganizerException("you are not an organizer of this party!");
            }
        }

        /**
         * checks if deleting request already exists in db
         * and throwing an error if this condition is true
         * @param partyId - identification of the party that the organizer wants to remove
         */
        private void isDeletingRequestExist(int partyId) {
            deletingRequestRepository.findByPartyId(partyId)
                    .ifPresent(ex -> {
                        throw new RequestAlreadyExistException("request with partyId: "
                                + partyId +
                                " already exist");
                    });
        }

        private void checkEventDate(PartyDto partyDto) {
            if (getCurrentDateTime().isAfter(partyDto.getDateOfEvent())
                    || getCurrentDateTime().equals(partyDto.getDateOfEvent())) {
                throw new IllegalArgumentException("entered data is not valid!");
            }
        }

        private LocalDateTime getCurrentDateTime() {
            return LocalDateTime.now();
        }

        private List<PartyDto> getConvertedList(List<PartyEntity> partyList) {
            List<PartyDto> partyDtoList = new ArrayList<>();
            partyList.stream().map(this::convertPartyToDto).forEach(partyDtoList::add);
            return partyDtoList;
        }

        private PartyDto convertPartyToDto(PartyEntity party) {

            return modelMapper.map(party, PartyDto.class);
        }

        private PartyEntity convertToParty(PartyDto partyDto, PersonModel organizer) {
            PartyEntity party = new PartyEntity();
            party.setOrganizer(organizer);
            party.setImages(partyDto.getImages());
            party.setType(personService.getPartyType(partyDto.getType()));
            party.setDateOfEvent(formatDateOfEvent(partyDto.getDateOfEvent()));
            party.setStatus(getPartyStatus(PartyStatusEnum.UNAVAILABLE.name()));
            modelMapper.map(partyDto, party);
            return party;
        }

        private PartyStatusModel getPartyStatus(String statusName){
            return partyStatusRepository.findByStatus(statusName)
                    .orElseThrow(() -> new StatusNotFoundException("status not found"));
        }

        private LocalDateTime formatDateOfEvent(LocalDateTime dateOfEvent){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formatedDate = dateOfEvent.format(formatter);
            return LocalDateTime.parse(formatedDate, formatter);
        }
}