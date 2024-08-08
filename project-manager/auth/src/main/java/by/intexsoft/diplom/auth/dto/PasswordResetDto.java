package by.intexsoft.diplom.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDto {

        private String username;
        private String oldPassword;
        private String newPassword;
        private String confirmPassword;
}
