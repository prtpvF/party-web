package by.intexsoft.diplom.common.model.status;

import by.intexsoft.diplom.common.model.party.PartyDraftModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyDraftStatusModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String name;

        @OneToOne(mappedBy = "status", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private PartyDraftModel partyDraft;
}
