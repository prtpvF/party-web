package by.intexsoft.diplom.publicapi.service;

import by.intexsoft.diplom.common.model.enums.PersonRolesEnum;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.common.repository.party.PartyRepository;
import by.intexsoft.diplom.common.repository.person.PersonRepository;
import by.intexsoft.diplom.publicapi.dto.PartyDto;
import by.intexsoft.diplom.publicapi.exception.*;
import by.intexsoft.diplom.publicapi.util.ObjectMapper;
import by.intexsoft.diplom.publicapi.util.ParticipationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PartyService {

        private final PartyRepository partyRepository;
        private final LocationService locationService;
        private final PersonRepository personRepository;
        private final PersonService personService;
        private final ParticipationRequestService participationRequestService;
        private final ObjectMapper objectMapper;

        public Page<PartyDto> getPartyInPersonCityByIp(String ip,
                                                       Principal principal,
                                                       Pageable pageable) {
            String city = ip.isBlank() ? getCityFromPrincipal(principal) : locationService.getLocationByIp(ip).getCity();
            return getPartiesByCity(city, pageable);
        }

        public Page<PartyDto> getPartyByCity(String city, Pageable pageable) {
            return getPartiesByCity(city, pageable);
        }

        public PartyDto getPartyById(int id, Principal principal) {
            PartyEntity party = partyRepository.findById(id)
                    .orElseThrow(() -> new PartyNotFoundException("party with this id not found"));

            PersonModel person = personService.getPersonByPrincipal(principal);

            if (isRoleOrganizerOrAdmin(person)) {

                isPartyBelongsToOrganizer(principal, party);
                return objectMapper.convertPartyToDtoForOrganizer(party);

            } else {
                PartyDto partyDto = objectMapper.convertPartyToDto(party);
                partyDto.setStatusOfParticipationRequest(
                        setStatusForPersonParticipation(person, party));
                return partyDto;
            }
        }

        public Page<PartyDto> getPartiesByName(String name, Pageable pageable) {
            Page<PartyEntity> page = partyRepository.findAllAvailableByName(name, pageable);
            validatePageNumber(page, pageable.getPageNumber());
            isPageEmpty(page);
            return page.map(party -> objectMapper.convertPartyToDto(party));
        }

        public Page<PartyDto> getPartiesByType(String type, Pageable pageable) {
            Page<PartyEntity> page = partyRepository.findAllAvailableByType(type, pageable);
            validatePageNumber(page, pageable.getPageNumber());
            isPageEmpty(page);
            return page.map(party -> objectMapper.convertPartyToDto(party));
        }

        private Page<PartyDto> getPartiesByCity(String city, Pageable pageable) {
            Page<PartyEntity> page = partyRepository.findAllAvailableByCity(city, pageable);
            validatePageNumber(page, pageable.getPageNumber());
            isPageEmpty(page);
            return page.map(party -> objectMapper.convertPartyToDto(party));
        }

        private void isPageEmpty(Page page) {
            if (page.isEmpty()) {
                throw new NoPartiesFoundException("No parties found");

            }
        }

        private String getCityFromPrincipal(Principal principal) {
            PersonModel person = personRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new PersonNotFoundException("person with this username not found"));
            return person.getCity();
        }

        private void validatePageNumber(Page<?> page, int pageNumber) {
            if (page.getTotalPages() <= pageNumber) {
                throw new UnavailablePageNumberException("Page doesn't exist");
            }
        }

        private void isPartyBelongsToOrganizer(Principal principal,
                                               PartyEntity party) {
            PersonModel organizer = personService.getPersonByPrincipal(principal);
            if(!party.getOrganizer().equals(organizer)) {
                throw new IllegalPartyOrganizerException(
                        "you are not the organizer of this party!");
            }
        }

        private String setStatusForPersonParticipation(PersonModel person,
                                                       PartyEntity party) {
            ParticipationRequestModel request = participationRequestService.findByPersonAndParty(person, party);
            String status = "";
            switch (request.getStatus().getStatusName()) {
                case ("ACCEPTED"):
                    status = ParticipationStatus.ACCEPTED.name();
                    break;
                case ("IN_PROGRESS"):
                    status = ParticipationStatus.IN_PROGRESS.name();
                    break;
                case("REJECTED"):
                    status = ParticipationStatus.REJECTED.name();
                    break;
                default:
                    break;
            }
            return status;
        }

        private boolean isRoleOrganizerOrAdmin(PersonModel person) {
            String roleName = person.getRole().getRoleName();
            return PersonRolesEnum.ORGANIZER.name().equals(roleName) ||
                    PersonRolesEnum.ADMIN.name().equals(roleName);
        }
}