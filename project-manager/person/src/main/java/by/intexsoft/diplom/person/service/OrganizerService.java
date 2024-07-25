package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common_module.model.*;
import by.intexsoft.diplom.common_module.model.enums.PartyStatusEnum;
import by.intexsoft.diplom.common_module.repository.*;
import by.intexsoft.diplom.person.dto.OrgAnswerDto;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
         * @param token - identifier of organizer
         * @param partyCreateDto - object what will be converted in Party model
         * @return HttpStatus
         */
        public HttpStatus createPartyRequest(String token, PartyDto partyCreateDto) {
            Person organizer = personService.getPersonFromToken(token);
            Party party = convertToParty(partyCreateDto, organizer);
            checkEventDate(partyCreateDto);
            partyRepository.save(party);
            return HttpStatus.CREATED;
        }

        /**
         * method creates a request to delete a party
         * by retrieving Party model from DB and calls validating method
         * @param token - identifier of organizer
         * @param partyId - identifier of an instance of Party what will be retrieved from DB
         * @return HttpStatus
         */
        public HttpStatus createPartyDeleteRequest(int partyId, String token) {
            DeletingPartyRequest request = new DeletingPartyRequest();
            prepareDeletingRequest(request, token, partyId);
            deletingRequestRepository.save(request);
            return HttpStatus.OK;
        }

        public HttpStatus updateParty(int partyId, String token,
                                      PartyDto partyDto) {
            Party party = personService.findPartyById(partyId);
            checkPartyOwner(token, partyId);
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
         * @param token - encrypted person username
         * @param orgAnswerDto - Dto of organizer's decision flag (accept if flag = true, refuse if flag = false)
         * @return HttpStatus
         */
        public HttpStatus answerRequest(int requestId, String token,
                                        OrgAnswerDto orgAnswerDto) {
            ParticipationRequest request = findRequestById(requestId);
            ParticipationRequestDto dto = modelMapper.map(request, ParticipationRequestDto.class);
            Person organizer = personService.getPersonFromToken(token);
            if(orgAnswerDto.getAccept()){
                isRequestBelongToOrganizer(dto, organizer);
                Party party = retrievePartyFromRequest(dto);
                Person guest = retrievePersonFromRequest(dto);
                addPersonToPartyGuest(guest, party);
                //todo send notification about accepting
                return HttpStatus.CREATED;
            }
            //todo send notification about rejecting
            return HttpStatus.OK;
        }

        public HttpStatus deleteRequest(int requestId, String token) {
            Person organizer = personService.getPersonFromToken(token);
            ParticipationRequestDto requestDto = personService.getValidatedPersonParticipationRequest(requestId);
            isRequestBelongToOrganizer(requestDto, organizer);
            requestRepository.deleteById(requestId);
            return HttpStatus.OK;
        }

        private void addPersonToPartyGuest(Person person, Party party) {
            party.getGuests().add(person);
            person.getParties().add(party);
            partyRepository.save(party);
            personRepository.save(person);
        }

        private void isRequestBelongToOrganizer(ParticipationRequestDto request,
                                                Person organizer) {
            Party partyFromRequest = personService.findPartyById(request.getPartyId());
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
        private Party retrievePartyFromRequest(ParticipationRequestDto request){
            return personService.findPartyById(request.getPartyId());
        }

        /**
         * method retrieves person model from participation request
         * @param request - ParticipationRequest object
         * @return founded person
         */
        private Person retrievePersonFromRequest(ParticipationRequestDto request) {
            return personService.findPersonById(request.getPersonId());
        }

        /**
         * method find a participation request by id in DB
         * or throw an exception if nothing found
         * @param requestId - identifier of participate request
         * @return founded participate request
         */
        private ParticipationRequest findRequestById(int requestId) {
            return requestRepository.findById(requestId)
                    .orElseThrow(() -> new RequestNotFoundException
                            ("participate request with this id doesnt exist"));
        }

        private void prepareDeletingRequest(DeletingPartyRequest request, String token, int partyId) {
            request.setOrganizer(personService.getPersonFromToken(token));
            request.setParty(personService.findPartyById(partyId));
            checkPartyOwner(token, partyId);
            isDeletingRequestExist(partyId);
        }

        /**
         * This method checks whether the organizer retrieved from the token
         * corresponds to the true party’s organizer.
         * @param token - identification of a person
         * @param partyId - identification of a party
         */
        private void checkPartyOwner(String token, int partyId) {
            Party party = personService.findPartyById(partyId);
            Person organizer = personService.getPersonFromToken(token);
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

        private List<PartyDto> getConvertedList(List<Party> partyList) {
            List<PartyDto> partyDtoList = new ArrayList<>();
            partyList.stream().map(this::convertPartyToDto).forEach(partyDtoList::add);
            return partyDtoList;
        }

        private PartyDto convertPartyToDto(Party party) {
            return modelMapper.map(party, PartyDto.class);
        }

        private Party convertToParty(PartyDto partyDto, Person organizer) {
            Party party = new Party();
            party.setOrganizer(organizer);
            party.setImages(partyDto.getImages());
            party.setType(personService.getPartyType(partyDto.getType()));
            party.setDateOfEvent(formatDateOfEvent(partyDto.getDateOfEvent()));
            party.setStatus(getPartyStatus(PartyStatusEnum.UNAVAILABLE.name()));
            modelMapper.map(partyDto, party);
            return party;
        }

        private PartyStatus getPartyStatus(String statusName){
            return partyStatusRepository.findByStatus(statusName)
                    .orElseThrow(() -> new StatusNotFoundException("status not found"));
        }

        private LocalDateTime formatDateOfEvent(LocalDateTime dateOfEvent){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formatedDate = dateOfEvent.format(formatter);
            return LocalDateTime.parse(formatedDate, formatter);
        }
}