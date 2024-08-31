package by.intexsoft.diplom.person.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipationRequestDto {

        private int id;
        private  Integer guestId; /*id of person who sends participation request*/
        private String organizerUsername;
        private String statusName;
        private PartyDto partyDto;
}