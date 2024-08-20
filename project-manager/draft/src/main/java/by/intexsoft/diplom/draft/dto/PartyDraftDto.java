package by.intexsoft.diplom.draft.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyDraftDto {

        private Integer id;

        private String name;

        private int ageRestriction;

        private int countOfPlaces;

        private String description;

        private String city;

        private String address;

        private Integer typeId;

        private Integer ownerId;

        private Double minimalRating;

        private Integer statusId;

        private Double ticketCost;

        private LocalDateTime dateOfEvent;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private Integer partyId;
}