package by.intexsoft.diplom.publicapi.dto;

import lombok.Getter;
import lombok.Setter;
import org.keycloak.representations.idm.UserRepresentation;

@Getter
@Setter
public class UpdateRequestDto {

        private PersonUpdateDto personUpdateDto;
        private UserRepresentation representation;
}
