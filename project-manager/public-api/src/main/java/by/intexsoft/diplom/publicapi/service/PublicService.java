package by.intexsoft.diplom.publicapi.service;

import by.intexsoft.diplom.common_module.model.Party.Party;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.model.role.PartyType;
import by.intexsoft.diplom.common_module.repository.PartyRepository;
import by.intexsoft.diplom.common_module.repository.PersonRepository;
import by.intexsoft.diplom.publicapi.dto.PartyDto;
import by.intexsoft.diplom.publicapi.dto.PersonDto;
import by.intexsoft.diplom.publicapi.exception.NoPartiesInCityException;
import by.intexsoft.diplom.publicapi.exception.PartyNotFoundException;
import by.intexsoft.diplom.publicapi.exception.PersonNotFoundException;
import by.intexsoft.diplom.publicapi.util.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicService  {
    private final PartyRepository partyRepository;
    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public List<PartyDto> getPartyInPersonCity(String city){
        List<Party> foundedParties = partyRepository.findAllByCity(city);
        isPartyListEmpty(foundedParties, city);
        return  objectMapper.convertPartyListToDto(foundedParties);
    }

    public PartyDto getParty(int id) {
        Party party = checkEntityExists(
                id,
                partyRepository::findById,
                () -> new PartyNotFoundException("Party with this id not found")
        );
        return objectMapper.convertPartyToDto(party);
    }

    public PersonDto getPerson(int id) {
        Person person = checkEntityExists(
                id,
                personRepository::findById,
                () -> new PersonNotFoundException("Person with this id not found")
        );
        return objectMapper.convertPersonToDto(person);
    }


    private <T> T checkEntityExists(int id, Function<Integer, Optional<T>> findByIdFunction, Supplier<RuntimeException> exceptionSupplier) {
        return findByIdFunction.apply(id)
                .orElseThrow(exceptionSupplier);
    }

    public void isPartyListEmpty(List<Party> parties, String city){
        if (parties.isEmpty()){
            throw new NoPartiesInCityException("no party in " + city);
        }
    }



}
