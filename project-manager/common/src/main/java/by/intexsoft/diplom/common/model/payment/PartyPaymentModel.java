package by.intexsoft.diplom.common.model.payment;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.person.PersonModel;
import by.intexsoft.diplom.common.model.enums.OperationStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyPaymentModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "person_id", nullable = false)
        private PersonModel person;

        @ManyToOne
        @JoinColumn(name = "party_id", nullable = false)
        private PartyEntity party;

        @ManyToOne
        @JoinColumn(name = "card_id", nullable = false)
        private CardModel card;

        private double amount;

        private LocalDateTime created_at;

        @NotNull(message = "status can't be empty")
        private OperationStatusEnum status;
}
