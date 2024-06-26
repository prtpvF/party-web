package com.by.chaplygin.demo.Model;

import com.by.chaplygin.demo.Enums.PersonRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Organizer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organizer  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "age")
    private int age;

    @Column(name = "email")
    @Email
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



    @OneToMany(mappedBy = "organizer")
    @JsonBackReference(value = "org-party")
    private List<Party> allOrgParty = new ArrayList<>();

    @OneToMany(mappedBy = "organizer")
    @JsonIgnore
    private List<Bans> bans = new ArrayList<>();



}
