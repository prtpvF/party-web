package by.intexsoft.diplom.common.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "card")
@Data
public class CardModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "card")
    private List<OperationModel> operations = new ArrayList<>();
}
