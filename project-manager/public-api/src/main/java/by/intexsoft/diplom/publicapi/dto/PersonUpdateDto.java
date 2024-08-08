package by.intexsoft.diplom.publicapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonUpdateDto {

        private String username;
        private String email;
        private Integer age;
        private boolean organizer;
        private String city;
}
