package com.party.service.party_service.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.party.service.party_service.dto.OrganizerDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.Length;


import java.time.LocalDateTime;
import java.util.*;

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
    //@JdbcTypeCode(SqlTypes.VARCHAR)
    private int id;
    @NotBlank(message = "Name cannot be blank")
    @Length(min = 4, message = "Name must be at least 4 characters long")
    private String name;
    @Column(name = "date_of_create")
    private LocalDateTime dateOfCreate;
    @Column(name = "date_of_event")
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "Date of Event cannot be null")
    @FutureOrPresent(message = "Date of Event must be a future or present date")
    private LocalDateTime dateOfEvent;

    private String address;
    @Column(name = "age_restriction")
    @Min(0)
    @Max(99)
    private int ageRestriction;
    private String description;
    private String instagram;
    @NotBlank(message = "City cannot be blank")
    @Size(min = 3, max = 15, message = "City cannot be shorter then 3 and longer then 15")
    private String city;

    @Column(name = "organizer_id")
    @NotNull
    private Integer organizer;

    @ElementCollection
    @CollectionTable(name = "party_guests", joinColumns = @JoinColumn(name = "party_id"))
    @Column(name = "guest_id")
    private Set<Integer> guests = new HashSet<>();


    public void addGuest(int guestId) {
        guests.add(guestId);
    }

    public void removeGuest(int guestId) {
        guests.remove(guestId);
    }

    public boolean hasGuest(int guestId) {
        return guests.contains(guestId);
    }
}

