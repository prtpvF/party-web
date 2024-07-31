package by.intexsoft.diplom.common.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
        private PersonModel person;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "party_id", nullable = false)
        private PartyEntity party;

        public ParticipationRequestModel(PartyEntity party, PersonModel person) {
            this.party = party;
            this.person = person;
        }

        @Override
        public String toString() {
                return "ParticipationRequest{" +
                        "id=" + id +
                        '}';
        }
}
