package com.by.chaplygin.demo.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Party")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "date_of_create")
    private LocalDateTime dateOfCreate;
    @Column(name = "date_of_event")
    private LocalDateTime dateOfEvent;
    @Column(name = "address")
    private String address;
    @Column(name = "age_restriction")
    private int ageRestriction;
    @Column(name = "description")
    private String description;
    @Column(name = "instagram")
    private String instagram;
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person organizer;
}
