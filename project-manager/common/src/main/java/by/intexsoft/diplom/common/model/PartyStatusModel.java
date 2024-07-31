package by.intexsoft.diplom.common.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "party_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyStatusModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String status;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private List<PartyEntity> parties = new ArrayList<>();

    public PartyStatusModel(String status) {
        this.status = status;
    }
}
