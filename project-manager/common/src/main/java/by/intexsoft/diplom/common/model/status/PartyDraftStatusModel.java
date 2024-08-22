package by.intexsoft.diplom.common.model.status;

import by.intexsoft.diplom.common.model.draft.PartyCreateDraftModel;
import by.intexsoft.diplom.common.model.draft.PartyUpdateDraftModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

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


        @OneToMany(mappedBy = "status", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
        private List<PartyUpdateDraftModel> partyDraft = new ArrayList<>();

        @OneToMany(mappedBy = "status", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
        private List<PartyCreateDraftModel> partyCreateDraft = new ArrayList<>();

}
