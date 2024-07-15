package by.intexsoft.diplom.publicapi.dto;

import by.intexsoft.diplom.common_module.model.Image;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.model.role.PartyType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class PartyDto {
    private int id;
    private String name;
    private PartyType type;
    private Person organizer;
    private int ageRestriction;
    private int countOfPlaces;
    private String description;
    private String city;
    private String address;
    private double ticketCost;
    private LocalDateTime dateOfEvent;
    private Set<Image> images = new HashSet<>();

}
