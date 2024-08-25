package by.intexsoft.diplom.publicapi.dto;

import by.intexsoft.diplom.common.model.party.ImageModel;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.role.PartyTypeModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyDto {

        private int id;
        private String name;
        private String type;
        private String organizerUsername;
        private Integer ageRestriction;
        private Set<GuestDto> guests = new HashSet<>();
        private Integer countOfPlaces;
        private String description;
        private String city;
        private String coordinates;
        private Double minimalRating;
        private Double ticketCost;
        private LocalDateTime dateOfEvent;
        private String imagePath;
        private String statusOfParticipationRequest;

}
