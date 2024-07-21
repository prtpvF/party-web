package by.intexsoft.diplom.publicapi.dto;

import by.intexsoft.diplom.common_module.model.Person;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonDto {
    private int id;
    private String username;
    private int age;
    private double rating;
    private String city;
    private List<Person> friends = new ArrayList<>();
}
