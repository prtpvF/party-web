package by.intexsoft.diplom.person.service.request;

import by.intexsoft.diplom.common.model.ImageModel;
import by.intexsoft.diplom.common.model.PartyEntity;
import by.intexsoft.diplom.common.model.PartyStatusModel;
import by.intexsoft.diplom.common.model.PersonModel;
import by.intexsoft.diplom.common.model.enums.PartyStatusEnum;
import by.intexsoft.diplom.common.repository.PartyRepository;
import by.intexsoft.diplom.common.repository.PartyStatusRepository;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.exception.IllegalDataOfEventException;
import by.intexsoft.diplom.person.exception.PartyNotFoundException;
import by.intexsoft.diplom.person.exception.RequestAlreadyExistException;
import by.intexsoft.diplom.person.exception.StatusNotFoundException;
import by.intexsoft.diplom.person.kafka.KafkaMessageModel;
import by.intexsoft.diplom.person.service.DropBoxService;
import by.intexsoft.diplom.person.service.PersonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Class where all organizer's request are storing (CRUD request)
 * @version 1.0
 * @author Mihail Chaplygin
 */
@Service
@RequiredArgsConstructor
public class CrudPartyRequestService {

        private final PersonService personService;
        private final PartyRepository partyRepository;
        private final PartyStatusRepository partyStatusRepository;
        private final DropBoxService dropBoxService;
        private final KafkaTemplate<String, KafkaMessageModel> kafkaTemplate;
        private final ModelMapper modelMapper;

        private static final String FLAG_CREATE = "create";
        private static final String FLAG_UPDATE = "update";
        private static final String FLAG_DELETE = "delete";

        @Value("${spring.kafka.topic-admin.name}")
        private String adminTopic;

        @Value("${party-create-message}")
        private String partyCreateMessage;

        @Value("${party-delete-message}")
        private String partyDeleteMessage;

        @Value("${party-update-message}")
        private String partyUpdateMessage;

        /**
         * method creates a request to create party
         * by retrieving and converting PartyDto in Party model
         * @param principal  authenticated user
         * @param partyCreateDto  object what will be converted in Party model
         * @return HttpStatus
         */
        @Transactional
        public HttpStatus createPartyRequest(Principal principal,
                                             PartyDto partyCreateDto) {
            PersonModel organizer = personService.getPersonByPrincipal(principal);
            PartyEntity party = processPartyCreating(partyCreateDto, organizer);
            checkPartyCreationEligibility(organizer, partyCreateDto);
            partyRepository.save(party);
            sendMessageToAdmins(principal, FLAG_CREATE);
            return HttpStatus.CREATED;
        }

        /**
         * method delete party from db without admin's permission if party hasn't payments.
         * If party has any payments then method updates party status on WAITING_FOR_DELETING
         * by retrieving Party model from DB and calls validating method
         * @param principal  authenticated user
         * @param partyId  identifier of an instance of Party what will be retrieved from DB
         * @return HttpStatus
         */
        public HttpStatus createPartyDeleteRequest(int partyId, Principal principal) {
            PartyEntity party = getPartyById(partyId);
            personService.checkPartyOwner(principal, partyId);
            isDeletingRequestExist(partyId);
            processPartyDeleting(party);
            sendMessageToAdmins(principal, FLAG_DELETE);
            return HttpStatus.OK;
        }

        private void processPartyDeleting(PartyEntity party) {
            if(party.getPayments().isEmpty()) {
                partyRepository.delete(party);
                deletePartyFilesFromCloud(party);
            }
            party.setStatus(new PartyStatusModel(PartyStatusEnum
                    .WAIT_FOR_DELETING
                    .name()));
            partyRepository.save(party);
        }

        private PartyEntity getPartyById(int partyId) {
            return partyRepository.findById(partyId)
                    .orElseThrow(() -> new PartyNotFoundException(
                            "cannot find party with this id"));
        }

        public HttpStatus updateParty(int partyId,
                                      Principal principal,
                                      PartyDto partyDto) {
            PartyEntity party = personService.findPartyById(partyId);
            personService.checkPartyOwner(principal, partyId);
            modelMapper.map(partyDto, party);
            partyRepository.save(party);
            sendMessageToAdmins(principal, FLAG_UPDATE);
            return HttpStatus.OK;
        }

        private void checkPartyCreationEligibility(PersonModel personModel,
                                                   PartyDto partyDto) {
           checkEventDate(partyDto);
           personService.isPersonBanned(personModel);
        }

        private void checkEventDate(PartyDto partyDto) {
            if (getCurrentDateTime().isAfter(partyDto.getDateOfEvent())
                    || getCurrentDateTime().equals(partyDto.getDateOfEvent())) {
                throw new IllegalDataOfEventException("entered data is not valid!");
            }
        }

        private LocalDateTime getCurrentDateTime() {
            return LocalDateTime.now();
        }

        private PartyEntity processPartyCreating(PartyDto partyDto, PersonModel organizer) {
            PartyEntity party = new PartyEntity();
            party.setOrganizer(organizer);
            party.setImages(partyDto.getImages());
            party.setType(personService.getPartyType(partyDto.getType()));
            party.setDateOfEvent(formatDateOfEvent(partyDto.getDateOfEvent()));
            party.setStatus(getPartyStatus(PartyStatusEnum.WAIT_FOR_CREATING.name()));
            party.setCity(normalizeStringField(partyDto.getCity()));
            party.setAddress(normalizeStringField(partyDto.getAddress()));
            modelMapper.map(partyDto, party);
            return party;
        }

        private String normalizeStringField(String field) {
            return field.substring(0, 1).toUpperCase() + field.substring(1);
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

        /**
         * checks if deleting request already exists in db
         * and throwing an error if this condition is true
         * @param partyId - identification of the party that the organizer wants to remove
         */
        private void isDeletingRequestExist(int partyId) {
          PartyEntity party = personService.findPartyById(partyId);
          if(party.getStatus().getStatus()
                  .equals(PartyStatusEnum.WAIT_FOR_DELETING.name())) {
              throw new RequestAlreadyExistException("this party is already in deleting list");
          }
        }

        private void sendMessageToAdmins(Principal principal, String flag) {
            KafkaMessageModel messageModel = new KafkaMessageModel();
            PersonModel organizer = personService.getPersonByPrincipal(principal);
            String data = "";
            messageModel.setTopic(adminTopic);
            messageModel.setUsername(organizer.getUsername());
            messageModel.setToEmail("random");
            switch (flag){
                case FLAG_CREATE:
                    data = partyCreateMessage;
                    break;
                case FLAG_DELETE:
                    data = partyDeleteMessage;
                    break;
                case FLAG_UPDATE:
                    data = partyUpdateMessage;
                    break;
                default:
                    break;
            }
            messageModel.setData(String.format(data,
                    organizer,
                    "http://localhost:8080"));
            kafkaTemplate.send(adminTopic, messageModel);
        }

        private void deletePartyFilesFromCloud(PartyEntity party) {
            Set<ImageModel> images = party.getImages();

            for (ImageModel image : images) {
                dropBoxService.deleteFile(image.getName());
            }
        }
}
