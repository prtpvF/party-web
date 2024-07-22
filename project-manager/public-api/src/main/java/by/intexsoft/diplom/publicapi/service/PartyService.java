package by.intexsoft.diplom.publicapi.service;

import by.intexsoft.diplom.common_module.model.Party.Party;
import by.intexsoft.diplom.common_module.repository.PartyRepository;
import by.intexsoft.diplom.publicapi.dto.PartyDto;
import by.intexsoft.diplom.publicapi.exception.NoPartiesInCityException;
import by.intexsoft.diplom.publicapi.exception.PartyNotFoundException;
import by.intexsoft.diplom.publicapi.util.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public List<PartyDto> getPartyInPersonCity(String city) {
        List<Party> foundedParties = partyRepository.findAllByCity(city);
        isPartyListEmpty(foundedParties, city);
        return  objectMapper.convertPartyListToDto(foundedParties);
    }

    public PartyDto getParty(int id) {
        Party party = partyRepository.findById(id).orElseThrow(()
                -> new PartyNotFoundException("party with this id not found"));
        return objectMapper.convertPartyToDto(party);
    }

    public void isPartyListEmpty(List<Party> parties, String city) {
        if (parties.isEmpty()){
            throw new NoPartiesInCityException("no party in " + city);
        }
    }
}