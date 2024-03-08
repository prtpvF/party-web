package com.by.chaplygin.demo.Services;

import com.by.chaplygin.demo.Exceptions.NotFoundException;
import com.by.chaplygin.demo.Model.Organizer;
import com.by.chaplygin.demo.Model.ParticipationRequests;
import com.by.chaplygin.demo.Model.Party;
import com.by.chaplygin.demo.Model.Person;
import com.by.chaplygin.demo.Repositories.OrganizerRepository;
import com.by.chaplygin.demo.Repositories.PartyRepository;
import com.by.chaplygin.demo.Repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganizerService {
    private final OrganizerRepository organizerRepository;
    private final PartyRepository partyRepository;
    private final RequestRepository requestRepository;

    //todo Отправить запрос на почтовый сервис для отправки сообщения
//    public HttpStatus answerRequest(String username, int partyId, int requestId){
//        Optional<Organizer> organizer = organizerRepository.findByUsername(username); // todo добавить исключение
//        Optional<Party> party = partyRepository.findById(partyId); //todo добавить исключение
//
//        if(party.get().getOrganizer()!=organizer.get()){
//            //todo исключение
//        }
//        else{
//            Optional<ParticipationRequests> request = requestRepository.findById(requestId);
//
//        }
//
//    }
//todo протестировать
    public HttpStatus acceptRequest(String username, int partyId, int requestId) throws NotFoundException {
        Optional<Organizer> organizer = Optional.ofNullable(organizerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("organizer not found")));

        Optional<Party> party = Optional.ofNullable(partyRepository.findById(partyId)
                .orElseThrow(() -> new NotFoundException("party not found")));

        Optional<ParticipationRequests> request = Optional.ofNullable(requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("request not found")));

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
        }
        return HttpStatus.OK;
    }
}
