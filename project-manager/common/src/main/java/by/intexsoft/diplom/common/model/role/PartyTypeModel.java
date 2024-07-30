package by.intexsoft.diplom.common.model.role;

import by.intexsoft.diplom.common.model.PartyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partyType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyTypeModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String type;

        @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
        private List<PartyEntity> parties = new ArrayList<>();

        public PartyTypeModel(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
                return "PartyType{" +
                        "id=" + id +
                        ", type='" + type + '\'' +
                        '}';
        }
}
