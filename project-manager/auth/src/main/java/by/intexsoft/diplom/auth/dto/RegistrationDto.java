package by.intexsoft.diplom.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.keycloak.representations.idm.CredentialRepresentation;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDto  {

        private int age;

        private String city;

        private boolean organizer;

        private String password;

        private String username;

        private String email;

        private boolean enabled;

        private List<String> groups;

        private Boolean emailVerified;

        private List<CredentialRepresentation> credentials = new ArrayList<>();

        private List<String> roles = new ArrayList<>();
}
