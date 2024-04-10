package com.auth.authmicroservice.Model;

import com.auth.authmicroservice.PersonRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    @NotEmpty(message = "username cannot be empty")
    @Length(min=3, max=24, message = "min length of username = 3, and max=24")
    private String username;

    @Column(name = "age")
    @Min(value = 16, message = "min age is 16")
    @Max(value = 100, message = "max age is 100")
    private int age;

    @Column(name = "email")
    @Email(message = "email is not correct")
    private String email;

    @Column(name="phone")
    @JsonIgnore
    private String phone;

    @Column(name = "date_of_create")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSS")
    @JsonIgnore
    private LocalDateTime dateOfCreate;

    @Column(name = "date_of_update")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSS")
    @JsonIgnore
    private LocalDateTime dateOfUpdate;

    @Column(name = "score")
    private double score;

    @Column(name = "is_active")
    @JsonIgnore
    private boolean isActive;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @ElementCollection(targetClass = PersonRole.class)
    @CollectionTable(name = "person_roles", joinColumns = @JoinColumn(name = "person_id"))
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private List<PersonRole> roles;

    @Column(name = "city")
    private String city;


}