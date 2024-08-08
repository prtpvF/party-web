package by.intexsoft.diplom.publicapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordResetDto {

        @NotBlank(message = "Field cannot be blank")
        private String oldPassword;

        @NotBlank(message = "Field cannot be blank")
        @Length(min = 5,  message = "password must be longer than 5 characters")
        private String newPassword;
}
