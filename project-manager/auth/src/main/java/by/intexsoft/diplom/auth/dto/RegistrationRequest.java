package by.intexsoft.diplom.auth.dto;

import lombok.Getter;
import lombok.Setter;
import org.keycloak.representations.idm.UserRepresentation;

@Getter
@Setter
public class RegistrationRequest {

        private RegistrationDto dto;
        private UserRepresentation representation;
}