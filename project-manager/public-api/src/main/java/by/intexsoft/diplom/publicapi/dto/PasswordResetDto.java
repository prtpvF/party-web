package by.intexsoft.diplom.publicapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetDto {


    @NotBlank(message = "Field cannot be blank")
    private String oldPassword;

    @NotBlank(message = "Field cannot be blank")
    private String newPassword;
}
