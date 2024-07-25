package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common_module.model.ParticipationRequest;
import by.intexsoft.diplom.common_module.model.Party;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.model.role.PartyType;
import by.intexsoft.diplom.common_module.repository.ParticipationRequestRepository;
import by.intexsoft.diplom.common_module.repository.PartyRepository;
import by.intexsoft.diplom.common_module.repository.PartyTypeRepository;
import by.intexsoft.diplom.common_module.repository.PersonRepository;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.exception.*;
import by.intexsoft.diplom.security.jwt.JwtUtil;
import com.auth0.jwt.exceptions.SignatureVerificationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

        private final ParticipationRequestRepository requestRepository;
        private final PartyTypeRepository partyTypeRepository;
        private final PartyRepository partyRepository;
        private final PersonRepository personRepository;
        private final JwtUtil jwtUtil;
        private final ModelMapper modelMapper;

        public HttpStatus sendParticipationRequest(int partyId, String token){
                Person person = getPersonFromToken(token);
                Party party = findPartyById(partyId);
                isRequestDataValid(person, party);
                requestRepository.save(new ParticipationRequest(party, person));
                return HttpStatus.CREATED;
        }

        public List<ParticipationRequestDto> getAllPersonParticipationRequests(String token) {
                Person person = getPersonFromToken(token);
                return convertListOfRequestToDto(person.getParticipationRequests());
        }

        public ParticipationRequestDto getValidatedPersonParticipationRequest(int participationRequestId) {
               return convertRequestToDto(findParticipationRequestById(participationRequestId));
        }

        public HttpStatus deleteParticipationRequest(int participationRequestId, String token) {
                ParticipationRequest participationRequest = findParticipationRequestById(participationRequestId);
                Person person = getPersonFromToken(token);
                isParticipateRequestBelongToPerson(participationRequest, person);
                requestRepository.delete(participationRequest);
                return HttpStatus.OK;
        }

        public String getUsernameFromToken(String token) {
                try {
                        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
                        jwtUtil.isTokenActive(username);
                        return username;
                }catch (SignatureVerificationException ex){
                        log.warn("something wrong with token");
                        return "error with token signature";
                }
        }

        /**
         * Organizer is instance of Person, so we find by PersonRepository
         * @param token - identification of a person
         * @return organizer model
         */
        public Person getPersonFromToken(String token) {
                String username = getUsernameFromToken(token);
                Person organizer = personRepository.findByUsername(username)
                        .orElseThrow(() -> new PersonNotFoundException("person with this username not found"));
                if (!organizer.isActive()) {
                        throw new AccountIsBannedException("you are banned");
                }
                return organizer;
        }

        public PartyType getPartyType(String typeName){
                return partyTypeRepository.findByType(typeName)
                        .orElseThrow(() -> new IllegalArgumentException("type mot found", null));
        }

        public Party findPartyById(int partyId){
                return partyRepository.findById(partyId)
                        .orElseThrow(() -> new PartyNotFoundException("party not found"));
        }

        public Person findPersonById(int personId){
                return personRepository.findById(personId)
                        .orElseThrow(() -> new PersonNotFoundException("person not found"));
        }

        private void isParticipateRequestBelongToPerson(ParticipationRequest participationRequest,
                                                        Person person){
                if(!participationRequest.getPerson().getUsername().equals(person.getUsername())) {
                        throw new InvalidRequestOwner("this participate request doesn't belong to you!");
                }
        }

        private List<ParticipationRequestDto> convertListOfRequestToDto(List<ParticipationRequest> requests) {
                List<ParticipationRequestDto> dtos = new ArrayList<>();
               for (ParticipationRequest request : requests) {
                       dtos.add(convertRequestToDto(request));
               }
               return dtos;
        }

        private ParticipationRequestDto convertRequestToDto(ParticipationRequest request) {
                return modelMapper.map(request, ParticipationRequestDto.class);
        }

        /**
         * method checks does participate request exist
         * if the request doesn't exist - throws ParticipationRequestNotFoundException
         * @param participationRequestId - identification of participate request
         * @return founded participate request
         */
        private ParticipationRequest findParticipationRequestById(int participationRequestId) {
                return requestRepository.findById(participationRequestId)
                        .orElseThrow(() -> new ParticipationRequestNotFoundException("cannot find participate" +
                                                                                     " request with this id"));
        }

        private void isRequestDataValid(Person person, Party party){
                isPersonRateValid(person, party);
                isRequestAlsoExist(person, party);
        }

        /**
         * checks person's rating equals or bigger than party's minimal rating
         * and throws an exception if person's rating less than party's
         * @param person - guest who wants to send a request to participate in the party
         * @param party  - party which receives all participation requests
         */
        private void isPersonRateValid(Person person, Party party){
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
        private void isRequestAlsoExist(Person person, Party party) {
                requestRepository.findByPersonAndParty(person, party)
                        .ifPresent(ex -> {
                                throw new ParticipationRequestAlsoExists("you can't send this"+
                                                                            "request for participation"+
                                                                            "because you have already"+
                                                                            "sent this request");
                        });
        }
}
