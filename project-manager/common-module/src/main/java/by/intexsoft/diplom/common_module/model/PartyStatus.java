package by.intexsoft.diplom.common_module.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "party_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartyStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String status;

    @OneToMany(mappedBy = "status")
    private List<Party> parties = new ArrayList<>();

    public PartyStatus(String status) {
        this.status = status;
    }
}
