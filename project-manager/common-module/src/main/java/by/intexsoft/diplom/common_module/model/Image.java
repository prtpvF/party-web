package by.intexsoft.diplom.common_module.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String name;

    @ManyToOne
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;
}
