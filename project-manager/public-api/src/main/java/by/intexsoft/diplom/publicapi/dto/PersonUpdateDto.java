package by.intexsoft.diplom.publicapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonUpdateDto {
    private String username;
    private String email;
    private Integer age;
    private boolean organizer;
    private String city;
}
