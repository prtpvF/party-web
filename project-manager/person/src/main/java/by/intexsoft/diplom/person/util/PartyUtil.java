package by.intexsoft.diplom.person.util;

import by.intexsoft.diplom.common_module.model.Party;
import by.intexsoft.diplom.common_module.model.PartyStatus;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.model.enums.PartyStatusEnum;
import by.intexsoft.diplom.common_module.model.role.PartyType;
import by.intexsoft.diplom.common_module.repository.PartyRepository;
import by.intexsoft.diplom.common_module.repository.PartyStatusRepository;
import by.intexsoft.diplom.common_module.repository.PartyTypeRepository;
import by.intexsoft.diplom.person.dto.PartyDto;
import by.intexsoft.diplom.person.exception.PartyNotFoundException;
import by.intexsoft.diplom.person.exception.StatusNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * this class help to manipulate party data and decreasing duplicated code
 * @author mihail chaplygin
 */
@Component
@RequiredArgsConstructor
public class PartyUtil {

        private final PartyStatusRepository partyStatusRepository;
        private final PartyRepository partyRepository;
        private final PartyTypeRepository partyTypeRepository;
        private final ModelMapper modelMapper;

        public Party convertToParty(PartyDto partyDto, Person organizer) {
            Party party = new Party();
            party.setOrganizer(organizer);
            party.setImages(partyDto.getImages());
            party.setType(getPartyType(partyDto.getType()));
            party.setDateOfEvent(formatDateOfEvent(partyDto.getDateOfEvent()));
            party.setStatus(getPartyStatus(PartyStatusEnum.UNAVAILABLE.name()));
            modelMapper.map(partyDto, party);
            return party;
        }

        public List<PartyDto> getConvertedList(List<Party> partyList) {
            List<PartyDto> partyDtoList = new ArrayList<>();
            partyList.stream().map(this::convertPartyToDto).forEach(partyDtoList::add);
            return partyDtoList;
        }


        private PartyDto convertPartyToDto(Party party) {
            return modelMapper.map(party, PartyDto.class);
        }

        private PartyStatus getPartyStatus(String statusName){
            return partyStatusRepository.findByStatus(statusName)
                    .orElseThrow(() -> new StatusNotFoundException("status not found"));
        }

        private PartyType getPartyType(String typeName){
            return partyTypeRepository.findByType(typeName)
                    .orElseThrow(() -> new IllegalArgumentException("type mot found", null));
        }

        private LocalDateTime formatDateOfEvent(LocalDateTime dateOfEvent){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formatedDate = dateOfEvent.format(formatter);
            return LocalDateTime.parse(formatedDate, formatter);
        }
}
