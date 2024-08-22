package by.intexsoft.diplom.common.model.draft;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "party_create_draft")
@Getter
@Setter
@NoArgsConstructor
public class PartyCreateDraftModel extends PartyDraftAbstract {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

}
