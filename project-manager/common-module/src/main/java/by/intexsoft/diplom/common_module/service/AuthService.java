package by.intexsoft.diplom.common_module.service;

import by.intexsoft.diplom.common_module.dto.RegistrationDto;
import by.intexsoft.diplom.common_module.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PersonRepository personRepository;

    public void registrate(RegistrationDto registrationDto){

    }

}
