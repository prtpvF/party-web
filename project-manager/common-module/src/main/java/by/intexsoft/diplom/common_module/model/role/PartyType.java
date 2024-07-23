package by.intexsoft.diplom.common_module.model.role;

import by.intexsoft.diplom.common_module.model.Party;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partyType")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartyType {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String type;

        @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
        private List<Party> parties = new ArrayList<>();

        public PartyType(String type) {
            this.type = type;
        }
}
