package by.intexsoft.diplom.common.model.status;

<<<<<<< HEAD
import by.intexsoft.diplom.common.model.draft.PartyCreateDraftModel;
import by.intexsoft.diplom.common.model.draft.PartyUpdateDraftModel;
=======
import by.intexsoft.diplom.common.model.party.PartyDraftModel;
>>>>>>> 3fde4c30443d2041b900952d15a715083e9858cf
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

=======
>>>>>>> 3fde4c30443d2041b900952d15a715083e9858cf
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

<<<<<<< HEAD
        @OneToMany(mappedBy = "status", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
        private List<PartyUpdateDraftModel> partyDraft = new ArrayList<>();

        @OneToMany(mappedBy = "status", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
        private List<PartyCreateDraftModel> partyCreateDraft = new ArrayList<>();
=======
        @OneToOne(mappedBy = "status", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private PartyDraftModel partyDraft;
>>>>>>> 3fde4c30443d2041b900952d15a715083e9858cf
}
