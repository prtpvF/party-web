package by.intexsoft.diplom.common.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participation_request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "person_id", nullable = false)
        @JsonBackReference
        private PersonModel personModel;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "party_id", nullable = false)
        private PartyModel partyModel;

        public ParticipationRequestModel(PartyModel partyModel, PersonModel personModel) {
            this.partyModel = partyModel;
            this.personModel = personModel;
        }

        @Override
        public String toString() {
                return "ParticipationRequest{" +
                        "id=" + id +
                        '}';
        }
}
