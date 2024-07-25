package by.intexsoft.diplom.person.dto;

import by.intexsoft.diplom.common_module.model.Image;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
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
        private Set<Image> images = new HashSet<>();
}