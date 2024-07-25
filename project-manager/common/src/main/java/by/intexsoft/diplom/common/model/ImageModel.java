package by.intexsoft.diplom.common.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "images")
@Data
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "party_id", nullable = false)
    private PartyModel partyModel;
}
