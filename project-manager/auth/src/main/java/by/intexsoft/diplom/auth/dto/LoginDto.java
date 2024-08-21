package by.intexsoft.diplom.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

        private final String grantType = "password";
        private final String clientId = "auth-app";
        private String username;
        private String password;
}
