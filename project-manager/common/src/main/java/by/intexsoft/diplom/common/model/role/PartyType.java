package by.intexsoft.diplom.common.model.role;

import by.intexsoft.diplom.common.model.PartyModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partyType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyType {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String type;

        @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
        private List<PartyModel> parties = new ArrayList<>();

        public PartyType(String type) {
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
