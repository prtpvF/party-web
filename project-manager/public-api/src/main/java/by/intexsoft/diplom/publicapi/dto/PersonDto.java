package by.intexsoft.diplom.publicapi.dto;

import by.intexsoft.diplom.common.model.PersonModel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

        private int id;
        private String username;
        private int age;
        private double rating;
        private String city;
        private List<PersonModel> friends = new ArrayList<>();
}
