package com.by.chaplygin.demo.Model;

import com.by.chaplygin.demo.Enums.PersonRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private String phone;
    @Column(name = "date_of_create")
    private LocalDateTime dateOfCreate;
    @Column(name = "date_of_update")
    private LocalDateTime dateOfUpdate;
    @Column(name = "score")
    private double score;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "password")
    private String password;

    @ElementCollection(targetClass = PersonRole.class)
    @CollectionTable(name = "person_roles", joinColumns = @JoinColumn(name = "person_id"))
    @Enumerated(EnumType.STRING)
    private List<PersonRole> roles;

    @OneToMany(mappedBy = "organizer")
    private List<Party> allOrgParty;




}
