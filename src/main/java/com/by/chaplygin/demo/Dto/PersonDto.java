package com.by.chaplygin.demo.Dto;

import com.by.chaplygin.demo.Enums.PersonRole;
import com.by.chaplygin.demo.Model.Person;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PersonDto {

    private String username;
    private int age;
    private String email;
    private String phone;
    private LocalDateTime dateOfCreate;
    private LocalDateTime dateOfUpdate;
    private double score;
    private boolean isActive;
    private String password;
    private List<PersonRole> roles;


    public Person personWithoutAllPartyField(Person person){
        Person personDto = new Person();
        personDto.setId(person.getId());
        personDto.setEmail(person.getEmail());
        personDto.setPhone(person.getPhone());
        personDto.setUsername(person.getUsername());
        return personDto;
    }
}
