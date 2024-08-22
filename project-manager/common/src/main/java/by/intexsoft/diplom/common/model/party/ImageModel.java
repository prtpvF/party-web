package by.intexsoft.diplom.common.model.party;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "images")
@Data
public class ImageModel {

    @Id
    private int id;

    private String name;

    @OneToOne
    @MapsId
    @JoinColumn(name = "party_id")
    private PartyEntity party;
}
