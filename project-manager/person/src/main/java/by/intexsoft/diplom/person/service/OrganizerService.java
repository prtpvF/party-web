package by.intexsoft.diplom.person.service;

import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.repository.party.PartyRepository;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.common.repository.request.ParticipationRequestRepository;
import by.intexsoft.diplom.person.dto.OrgAnswerDto;
import by.intexsoft.diplom.person.dto.ParticipationRequestDto;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.exception.*;
import by.intexsoft.diplom.person.kafka.KafkaMessageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizerService {

        private final PersonService personService;
        private final PartyRepository partyRepository;
        private final PersonRepository personRepository;
        private final ParticipationRequestRepository requestRepository;
        private final KafkaTemplate<String, KafkaMessageModel> kafkaTemplate;
        private final ModelMapper modelMapper;

        @Value("${spring.kafka.topic-participation.name}")
        private String participationTopic;

        @Value("${participation-request-decline}")
        private String decline;

        @Value("${participation-request-apply}")
        private String apply;

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
            ParticipationRequestModel request = findRequestById(requestId);
            ParticipationRequestDto dto = modelMapper.map(request,
                                          ParticipationRequestDto.class);
            PersonModel organizer = personService.getPersonByPrincipal(principal);

            if(Boolean.TRUE.equals(orgAnswerDto.getAccept())){
                isRequestBelongToOrganizer(dto, organizer);
                PartyEntity party = retrievePartyFromRequest(dto);
                PersonModel guest = retrievePersonFromRequest(dto);
                addPersonToPartyGuest(guest, party);
                sendNotificationAboutParticipationRequest(request,true);
                return HttpStatus.CREATED;
            }
            sendNotificationAboutParticipationRequest(request,false);
            return HttpStatus.OK;
        }

        public List<PartyDto> getMyParties(Principal principal) {
            PersonModel personModel = personService.getPersonByPrincipal(principal);
            return personModel.getParties(); //todo
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

        private void sendNotificationAboutParticipationRequest(ParticipationRequestModel request,
                                                               boolean flag) {
            PersonModel personFromRequest = request.getPerson();
            KafkaMessageModel kafkaMessage = new KafkaMessageModel();
            if(flag) {
                kafkaMessage.setData(String.format(apply,
                        personFromRequest.getUsername(),
                        request.getParty().getName()));
            }
            else {
                kafkaMessage.setData(String.format(decline,
                        personFromRequest.getUsername(),
                        request.getParty().getName()));
            }
            kafkaMessage.setTopic(participationTopic);
            kafkaMessage.setToEmail(personFromRequest.getEmail());
            kafkaMessage.setUsername(personFromRequest.getUsername());
            kafkaTemplate.send(participationTopic, kafkaMessage);
        }
}