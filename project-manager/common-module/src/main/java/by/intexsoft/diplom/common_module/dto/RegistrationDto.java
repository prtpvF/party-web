package by.intexsoft.diplom.common_module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {

    private String username;
    private String password;
    private int age;
    private String email;
    private boolean isAdmin;
}
