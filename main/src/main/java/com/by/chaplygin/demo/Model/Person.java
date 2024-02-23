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

    @ElementCollection(targetClass = PersonRole.class)
    @CollectionTable(name = "person_roles", joinColumns = @JoinColumn(name = "person_id"))
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private List<PersonRole> roles;



    @ManyToMany(mappedBy = "guests", fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonBackReference(value = "person-party")
    private Set<Party> allPersonParty = new HashSet<>();

    @OneToMany(mappedBy = "guestId")
    private List<ParticipationRequests> requests = new ArrayList<>();




}
