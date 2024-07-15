package by.intexsoft.diplom.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
    @NotBlank(message = "field can't be blank")
    private String username;
    @NotBlank(message = "field can't be blank")
    private String password;
    @Min(value = 14, message = "you must be older than 14")
    @Max(value = 100, message = "entered age isn't correct")
    private int age;
    @NotBlank(message = "field can't be blank")
    @Email(message = " field must be email type")
    private String email;
    @NotBlank
    @Length(min = 3, max = 15)
    private String city;
    private boolean isOrganizer;
}
