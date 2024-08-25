package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.common.model.role.PartyTypeModel;
import by.intexsoft.diplom.common.repository.party.PartyRepository;
import by.intexsoft.diplom.common.repository.party.PartyTypeRepository;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.common.repository.request.ParticipationRequestRepository;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

        private final ParticipationRequestRepository requestRepository;
        private final PersonRepository personRepository;
        private final ModelMapper modelMapper;
        private final PartyService partyService;

        public HttpStatus sendParticipationRequest(int partyId, Principal principal){
                PersonModel person = getPersonByPrincipal(principal);
                PartyEntity party = partyService.findPartyById(partyId);
                isRequestDataValid(person, party);
                requestRepository.save(new ParticipationRequestModel(party, person));
                return HttpStatus.CREATED;
        }

        public List<ParticipationRequestDto> getAllPersonParticipationRequests(Principal principal) {
                PersonModel person = getPersonByPrincipal(principal);
                return convertListOfRequestToDto(person.getParticipationRequests());
        }

        public ParticipationRequestDto getValidatedPersonParticipationRequest(int participationRequestId) {
               return convertRequestToDto(findParticipationRequestById(participationRequestId));
        }

        public HttpStatus deleteParticipationRequest(int participationRequestId,
                                                     Principal principal) {
                ParticipationRequestModel participationRequest = findParticipationRequestById(participationRequestId);
                PersonModel person = getPersonByPrincipal(principal);
                isParticipateRequestBelongToPerson(participationRequest, person);
                requestRepository.delete(participationRequest);
                return HttpStatus.OK;
        }

        public String getPersonUsername(Principal principal) {
                      return principal.getName();
        }

        /**
         * Organizer is instance of Person, so we find by PersonRepository
         * @param principal - authenticated user
         * @return organizer model
         */
        public PersonModel getPersonByPrincipal(Principal principal) {
                String username = getPersonUsername(principal);
                PersonModel organizer = personRepository.findByUsername(username)
                        .orElseThrow(() -> new PersonNotFoundException("person with this username not found"));
                isPersonBanned(organizer);
                return organizer;
        }

        public PersonModel findPersonById(int personId){
                return personRepository.findById(personId)
                        .orElseThrow(() -> new PersonNotFoundException("person not found"));
        }

        public void isPersonBanned(PersonModel personModel) {
                if(!personModel.isActive()) {
                        throw new AccountIsBannedException("you are banned");
                }
        }

        /**
         * This method checks whether the organizer retrieved from the token
         * corresponds to the true party’s organizer.
         * @param principal - authenticated user
         * @param partyId - identification of a party
         */
        public void checkPartyOwner(Principal principal, int partyId) {
                PartyEntity party = partyService.findPartyById(partyId);
                PersonModel organizer = getPersonByPrincipal(principal);
                if(!party.getOrganizer().equals(organizer)){
                        throw new IllegalPartyOrganizerException("you are not an organizer of this party!");
                }
        }

        private void isParticipateRequestBelongToPerson(ParticipationRequestModel participationRequest,
                                                        PersonModel person){
                if(!participationRequest.getPerson().getUsername().equals(person.getUsername())) {
                        throw new InvalidRequestOwner("this participate request doesn't belong to you!");
                }
        }

        private List<ParticipationRequestDto> convertListOfRequestToDto(List<ParticipationRequestModel> requests) {
                List<ParticipationRequestDto> dtos = new ArrayList<>();
               for (ParticipationRequestModel request : requests) {
                       dtos.add(convertRequestToDto(request));
               }
               return dtos;
        }

        private ParticipationRequestDto convertRequestToDto(ParticipationRequestModel request) {
                return modelMapper.map(request, ParticipationRequestDto.class);
        }

        /**
         * method checks does participate request exist
         * if the request doesn't exist - throws ParticipationRequestNotFoundException
         * @param participationRequestId - identification of participate request
         * @return founded participate request
         */
        private ParticipationRequestModel findParticipationRequestById(int participationRequestId) {
                return requestRepository.findById(participationRequestId)
                        .orElseThrow(() -> new ParticipationRequestNotFoundException("cannot find participate" +
                                                                                     " request with this id"));
        }

        private void isRequestDataValid(PersonModel person, PartyEntity party){
                isPersonRateValid(person, party);
                isRequestAlsoExist(person, party);
        }

        /**
         * checks person's rating equals or bigger than party's minimal rating
         * and throws an exception if person's rating less than party's
         * @param person - guest who wants to send a request to participate in the party
         * @param party  - party which receives all participation requests
         */
        private void isPersonRateValid(PersonModel person, PartyEntity party){
                if(person.getRating() < party.getMinimalRating()){
                        throw new IllegalArgumentException("your rating is less than the party's minimal rating");
                }
        }

        /**
         * Method checks request already exists or not
         * and throws exception if request exists
         * @param person - guest who have sent a request
         * @param party - party which stores all participation request
         */
        private void isRequestAlsoExist(PersonModel person, PartyEntity party) {
                requestRepository.findByPersonAndParty(person, party)
                        .ifPresent(ex -> {
                                throw new ParticipationRequestAlsoExists("you can't send this"+
                                                                            "request for participation"+
                                                                            "because you have already"+
                                                                            "sent this request");
                        });
        }
}
