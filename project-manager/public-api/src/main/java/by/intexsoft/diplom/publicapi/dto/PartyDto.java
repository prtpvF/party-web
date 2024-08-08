package by.intexsoft.diplom.publicapi.dto;

import by.intexsoft.diplom.common.model.ImageModel;
import by.intexsoft.diplom.common.model.PersonModel;
import by.intexsoft.diplom.common.model.role.PartyTypeModel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyDto {

        private int id;
        private String name;
        private PartyTypeModel type;
        private PersonModel organizer;
        private int ageRestriction;
        private int countOfPlaces;
        private String description;
        private String city;
        private String address;
        private double ticketCost;
        private LocalDateTime dateOfEvent;
        private Set<ImageModel> images = new HashSet<>();

}
