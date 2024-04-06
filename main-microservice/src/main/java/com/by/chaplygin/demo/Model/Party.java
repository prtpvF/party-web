package com.by.chaplygin.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSS")
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
    @JoinColumn(name = "organizer_id", nullable = false)
    @JsonBackReference(value = "org-party")
    private Organizer organizer;

    @ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "person_party",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "party_id")
    )
    @JsonBackReference(value = "person-party")
    private Set<Person> guests = new HashSet<>();

    @OneToMany(mappedBy = "partyId")
    private List<ParticipationRequests> requests = new ArrayList<>();
    @OneToMany(mappedBy = "party")
    private List<Report> reports = new ArrayList<>();
}
