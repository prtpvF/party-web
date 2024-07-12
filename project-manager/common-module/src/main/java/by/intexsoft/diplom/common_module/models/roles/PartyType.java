package by.intexsoft.diplom.common_module.models.roles;

import by.intexsoft.diplom.common_module.models.enums.PartyTypeEnum;
import by.intexsoft.diplom.common_module.models.Party.Party;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partyType")
@Data
public class PartyType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private PartyTypeEnum type;

    @OneToMany(mappedBy = "type")
    private List<Party> parties = new ArrayList<>();
}
