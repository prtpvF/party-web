package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common_module.model.Party;
import by.intexsoft.diplom.common_module.model.DeletingPartyRequest;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.model.role.PartyType;
import by.intexsoft.diplom.common_module.repository.DeletingPartyRequestRepository;
import by.intexsoft.diplom.common_module.repository.PartyRepository;
import by.intexsoft.diplom.common_module.repository.PartyTypeRepository;
import by.intexsoft.diplom.common_module.repository.PersonRepository;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.exception.*;
import by.intexsoft.diplom.security.jwt.JwtUtil;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created for organizer model
 * @author Chaplygin Mihail
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizerService {

        private final PersonRepository personRepository;
        private final PartyRepository partyRepository;
        private final PartyTypeRepository partyTypeRepository;
        private final DeletingPartyRequestRepository deletingRequestRepository;
        private final JwtUtil jwtUtil;
        private final ModelMapper modelMapper;

        public HttpStatus createParty( String token, PartyDto partyCreateDto) {
            Person organizer = getOrganizerByToken(token);
            Party party = convertToParty(partyCreateDto, organizer);
            checkEventDate(partyCreateDto);
            partyRepository.save(party);
            return HttpStatus.CREATED;
        }

        public HttpStatus createPartyDeleteRequest(int partyId, String token){
            DeletingPartyRequest request = new DeletingPartyRequest();
            prepareRequest(request, token, partyId);
            deletingRequestRepository.save(request);
            return HttpStatus.OK;
        }

        public HttpStatus updateParty(int partyId, String token, PartyDto partyDto){
            Party party = findPartyById(partyId);
            checkPartyOwner(token, partyId);
            modelMapper.map(partyDto, party);
            System.out.println(party);
            partyRepository.save(party);
            return HttpStatus.OK;
        }

        private void prepareRequest(DeletingPartyRequest request, String token, int partyId){
            request.setOrganizer(getOrganizerByToken(token));
            request.setParty(findPartyById(partyId));
            checkPartyOwner(token, partyId);
            isRequestExist(partyId);
        }

        /**
         * This method checks whether the organizer retrieved from the token
         * corresponds to the true party’s organizer.
         * @param token
         * @param partyId
         */
        private void checkPartyOwner(String token, int partyId){
            Party party = findPartyById(partyId);
            Person organizer = getOrganizerByToken(token);
            if(!party.getOrganizer().equals(organizer)){
                throw new IllegalPartyOrganizerException("you are not an organizer of this party!");
            }
        }

        private void isRequestExist(int partyId){
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

        private Party convertToParty(PartyDto partyDto, Person organizer) {
            Party party = new Party();
            party.setOrganizer(organizer);
            party.setImages(partyDto.getImages());
            party.setType(getType(partyDto.getType()));
            party.setDateOfEvent(formatDateOfEvent(partyDto.getDateOfEvent()));
            modelMapper.map(partyDto, party);
            return party;
        }

        private PartyType getType(String typeName){
            return partyTypeRepository.findByType(typeName)
                    .orElseThrow(() -> new IllegalArgumentException("type mot found", null));
        }

        private LocalDateTime formatDateOfEvent(LocalDateTime dateOfEvent){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formatedDate = dateOfEvent.format(formatter);
            return LocalDateTime.parse(formatedDate, formatter);
        }

        private String getUsernameFromToken(String token) {
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
         * @param token
         * @return organizer model
         */
        private Person getOrganizerByToken(String token) {
            String username = getUsernameFromToken(token);
            Person organizer = personRepository.findByUsername(username)
                    .orElseThrow(() -> new PersonNotFoundException("person with this username not found"));
            if (!organizer.isActive()) {
                throw new AccountIsBannedException("you are banned");
            }
            return organizer;
        }

        private Party findPartyById(int partyId) {
           return partyRepository.findById(partyId).orElseThrow(
                    () -> new PartyNotFoundException("cannot find party with id: " + partyId));
        }
}