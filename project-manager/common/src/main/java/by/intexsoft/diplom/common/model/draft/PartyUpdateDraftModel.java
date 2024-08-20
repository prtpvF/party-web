package by.intexsoft.diplom.common.model.draft;

import by.intexsoft.diplom.common.model.party.PartyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "party_draft")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyUpdateDraftModel extends PartyDraftAbstract{

        @Id
        private Integer id;

        @OneToOne
        @MapsId
        @JoinColumn(name = "party_id")
        private PartyEntity party;

}
