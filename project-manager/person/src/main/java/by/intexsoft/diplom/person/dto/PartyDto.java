package by.intexsoft.diplom.person.dto;

import by.intexsoft.diplom.common.model.party.ImageModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyDto {

        private int id;
        private String name;
        private String type;
        private Integer ageRestriction;
        private Integer countOfPlaces;
        private String description;
        private String city;
        private String address;
        private Double minimalRating;
        private Double ticketCost;
        private LocalDateTime dateOfEvent;
        private Set<ImageModel> images = new HashSet<>();
}