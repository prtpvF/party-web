package com.party.service.party_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
;
import java.time.LocalDateTime;
@Data
public class PartyDTO {
    private int id;
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 4, message = "Name must be at least 4 characters long")
    private String name;
    private LocalDateTime dateOfCreate;
    @NotNull(message = "Date of Event cannot be null")
    @FutureOrPresent(message = "Date of Event must be a future or present date")
    private LocalDateTime dateOfEvent;
    private String address;
    @Min(0)
    @Max(99)
    private int ageRestriction;
    private String description;
    private String instagram;
    @NotBlank(message = "City cannot be blank")
    @Size(min = 3, max = 15, message = "City cannot be shorter then 3 and longer then 15")
    private String city;
    @NotNull
    private Integer organizer;


}
