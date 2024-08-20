package by.intexsoft.diplom.draft.dto;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
=======
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

>>>>>>> 3fde4c30443d2041b900952d15a715083e9858cf
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyDraftDto {

<<<<<<< HEAD
        private Integer id;
=======
        private int id;
>>>>>>> 3fde4c30443d2041b900952d15a715083e9858cf

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