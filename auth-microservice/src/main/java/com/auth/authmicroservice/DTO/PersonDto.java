package com.auth.authmicroservice.DTO;

import lombok.Data;

@Data
public class PersonDto {
    private String username;
    private String password;
    private int age;
    private String email;
}
