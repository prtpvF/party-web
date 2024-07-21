package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common_module.model.Party.Party;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.repository.PartyRepository;
import by.intexsoft.diplom.common_module.repository.PersonRepository;
import by.intexsoft.diplom.person.dto.PartyCreateDto;
import by.intexsoft.diplom.person.exception.AccountIsBannedException;
import by.intexsoft.diplom.person.exception.PersonNotFoundException;
import by.intexsoft.diplom.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created for organizer model
 * @author Chaplygin Mihail
 */
@Service
@RequiredArgsConstructor
public class OrganizerService {

    private final PersonRepository personRepository;
    private final PartyRepository partyRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    public HttpStatus createParty(String token, PartyCreateDto partyCreateDto) {
        String username = getUsernameFromToken(token);
        Person organizer = getOrganizerByUsername(username);
        Party party = convertToParty(partyCreateDto, organizer);
        checkEventDate(partyCreateDto);
        partyRepository.save(party);
        return HttpStatus.CREATED;
    }

    private void checkEventDate(PartyCreateDto partyCreateDto) {
        if (getCurrentDateTime().isBefore(partyCreateDto.getDateOfEvent())
                || getCurrentDateTime() == partyCreateDto.getDateOfEvent()) {
            throw new IllegalArgumentException("entered data is not valid!");
        }
    }

    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    private Party convertToParty(PartyCreateDto partyCreateDto, Person organizer) {
        Party party = modelMapper.map(partyCreateDto, Party.class);
        party.setOrganizer(organizer);
        party.setImages(partyCreateDto.getImages());
        return party;
    }

    private String getUsernameFromToken(String token) {
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        jwtUtil.isTokenActive(username);
        return username;
    }

    /**
     * Organizer is instance of Person, so we find by PersonRepository
     * @param username
     * @return organizer model
     */
    private Person getOrganizerByUsername(String username) {
        Person organizer = personRepository.findByUsername(username)
                .orElseThrow(() -> new PersonNotFoundException("person with this username not found"));
        if (!organizer.isActive()) {
            throw new AccountIsBannedException("you are banned");
        }
        return organizer;
    }
}