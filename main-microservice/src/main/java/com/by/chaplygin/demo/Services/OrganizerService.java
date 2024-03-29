package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Exceptions.NotFoundException;
import com.by.chaplygin.demo.Model.*;
import com.by.chaplygin.demo.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganizerService {
    private final OrganizerRepository organizerRepository;
    private final PartyRepository partyRepository;
    private final RequestRepository requestRepository;
    private final PersonRepository personRepository;
    private final RestTemplate restTemplate;
    private final BansRepository bansRepository;



//todo протестировать
    @Transactional
    public HttpStatus acceptRequest(String username, int partyId, int requestId) throws NotFoundException {
        Optional<Organizer> organizer = getOrganizerByUsername(username);
        Optional<Party> party = getPartyById(partyId);
        Optional<ParticipationRequests> request = getRequestById(requestId);

        if (party.get().getOrganizer() != organizer.get()) {
            return HttpStatus.LOCKED;
        } else {

           Set<Person> guests = party.get().getGuests();
           guests.add(request.get().getGuestId());
           party.get().setGuests(guests);

           Set<Party> parties = request.get().getGuestId().getAllPersonParty();
           parties.add(party.get());
           request.get().getGuestId().setAllPersonParty(parties);

           request.get().getGuestId().getAllPersonParty().add(party.get());
           partyRepository.save(party.get());
           requestRepository.delete(request.get());
           answerRequest(username,partyId,requestId,"Answer", "REQUEST_ANSWER");
        }
        return HttpStatus.OK;
    }

    //todo доабвить circuitBrake
    private HttpStatusCode answerRequest(String username, int partyId, int requestId, String subject, String type){
        Optional<Person> person = personRepository.findByUsername(username);
        String email = person.get().getEmail();

        String serviceName = "http://gateway";
        String endpoint = "/email/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        params.add("email", email);
        params.add("subject", subject);
        params.add("type", type);
        params.add("partyId", partyId);
        params.add("requestId", requestId);

        ResponseEntity<Void> response = restTemplate.postForEntity(serviceName + endpoint, params, Void.class);
        return response.getStatusCode();
    }

    private Optional<Organizer> getOrganizerByUsername(String username){
        Optional<Organizer> organizer = Optional.of(new Organizer());
        try {
             organizer = Optional.ofNullable(organizerRepository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("organizer not found")));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        return organizer;
    }

    private Optional<Party> getPartyById(int partyId){
        Optional<Party> party = Optional.of(new Party());
        try {
            party = Optional.ofNullable(partyRepository.findById(partyId)
                    .orElseThrow(()-> new NotFoundException("party not found")));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        return party;
    }

    private Optional<ParticipationRequests> getRequestById(int requestId){
        Optional<ParticipationRequests> participationRequests = Optional.of(new ParticipationRequests());
        try {
            participationRequests = Optional.ofNullable(requestRepository.findById(requestId)
                    .orElseThrow(()-> new NotFoundException("request not found")));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        return  participationRequests;
    }

    public void banGuest(String username, int personId, String endOfBan){
        Optional<Person> guest = personRepository.findById(personId);
        Optional<Organizer> organizer = organizerRepository.findByUsername(username);
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = date.format(getCurrentDate());
        Bans ban = new Bans(guest.get(), organizer.get(), formattedDate, endOfBan);
        bansRepository.save(ban);
    }

    private Date getCurrentDate(){
        Date date = new Date();
        return date;
    }




}
