package by.intexsoft.diplom.auth.dto;

import lombok.Data;

@Data
public class PasswordResetDto {
    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
